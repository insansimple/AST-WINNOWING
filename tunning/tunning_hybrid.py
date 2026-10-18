import argparse
import csv
import json
import os
import sys
import time
from typing import List, Dict, Any, Tuple

# Pastikan path engine publikasi terdaftar di sys.path
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
PUBLIKASI_DIR = os.path.dirname(CURRENT_DIR)
ENGINE_DIR = os.path.join(PUBLIKASI_DIR, "engine")
DATASET_DIR = os.path.join(PUBLIKASI_DIR, "dataset")
FROZEN_CONFIG_PATH = os.path.join(PUBLIKASI_DIR, "frozen_config.json")
RESULTS_DIR = os.path.join(CURRENT_DIR, "results")

if ENGINE_DIR not in sys.path:
    sys.path.insert(0, ENGINE_DIR)

from dataset_utils import read_label
from winnowing_engine import StandaloneWinnowingEngine
from java_ast_engine import StandaloneJavaASTEngine, JavaASTNode


def rank_key(r: Dict[str, Any]) -> Tuple:
    """
    Kunci pengurutan tunggal (dipakai untuk CSV, leaderboard, dan pemilihan terbaik):
    1. Akurasi tertinggi (-r['accuracy'])
    2. Bypass rate tertinggi / penghematan komputasi terbesar (-r['bypass_rate'])
    3. avg_plag_similarity paling tinggi (-r['avg_plag_similarity'])
    4. avg_non_plag_similarity paling rendah (r['avg_non_plag_similarity'])
    5. lower_th dan upper_th sebagai tie-breaker deterministik
    """
    return (
        -r["accuracy"],
        -r["bypass_rate"],
        -r["avg_plag_similarity"],
        r["avg_non_plag_similarity"],
        r["lower_th"],
        r["upper_th"],
    )


def select_hybrid(
    rows: List[Dict[str, Any]],
    min_accuracy: float = 100.0
) -> Tuple[Dict[str, Any], List[Dict[str, Any]], List[Dict[str, Any]]]:
    """
    Kriteria pemilihan parameter Hybrid (lower_th dan upper_th):
      1. Batas aman keakuratan klasifikasi: accuracy >= min_accuracy (100.0%)
         (jika tidak ada yang 100%, seluruh kombinasi dipakai)
      2. Dari kandidat yang lolos, pilih kombinasi dengan bypass_rate tertinggi,
         disusul avg_plag_similarity tertinggi dan avg_non_plag_similarity terendah.
      3. Jika terdapat rentang nilai kembar (plato kestabilan), pilih nilai titik tengah (midpoint)
         antara batas bawah dan batas atas plato tersebut sebagai margin keamanan seimbang.

    Mengembalikan (best, ranked_all, cands):
      - best       : parameter terpilih (= baris pertama ranked_all)
      - ranked_all : seluruh kombinasi terurut
      - cands      : kandidat yang lolos filter akurasi 100%
    """
    cands = [r for r in rows if r["accuracy"] >= min_accuracy] or rows

    # Identifikasi kelompok performa puncak (akurasi dan bypass_rate tertinggi)
    max_acc = max(r["accuracy"] for r in cands)
    max_bypass = max(r["bypass_rate"] for r in cands if r["accuracy"] == max_acc)
    top_cands = [r for r in cands if r["accuracy"] == max_acc and r["bypass_rate"] == max_bypass]

    # Hitung midpoint (titik tengah) rentang lower_th pada kelompok terbaik
    min_lower = min(r["lower_th"] for r in top_cands)
    max_lower = max(r["lower_th"] for r in top_cands)
    midpoint_lower = (min_lower + max_lower) / 2.0  # Contoh: (0.18 + 0.25) / 2 = 0.215

    def candidate_rank_key(r: Dict[str, Any]) -> Tuple:
        return (
            -r["accuracy"],
            -r["bypass_rate"],
            -r["avg_plag_similarity"],
            r["avg_non_plag_similarity"],
            round(abs(r["lower_th"] - midpoint_lower), 4),  # Prioritaskan yang terdekat dengan titik tengah
            r["upper_th"],
            r["lower_th"],
        )

    cand_ids = {id(r) for r in cands}
    rest = [r for r in rows if id(r) not in cand_ids]
    ranked_all = sorted(cands, key=candidate_rank_key) + sorted(rest, key=candidate_rank_key)
    return ranked_all[0], ranked_all, cands


def update_frozen_config(best: Dict[str, Any], frozen_config_path: str = FROZEN_CONFIG_PATH) -> None:
    """
    Memperbarui parameter Fast-Exit Hybrid (lower_threshold, upper_threshold, dan threshold vonis)
    langsung ke frozen_config.json dengan tetap mempertahankan parameter konfigurasi lainnya.
    """
    if not frozen_config_path:
        return
    config = {}
    if os.path.exists(frozen_config_path):
        try:
            with open(frozen_config_path, "r", encoding="utf-8") as f:
                config = json.load(f)
        except Exception as e:
            print(f"[WARNING] Gagal membaca frozen config lama ({frozen_config_path}): {e}")
            config = {}

    config["lower_threshold"] = round(float(best["lower_th"]), 2)
    config["upper_threshold"] = round(float(best["upper_th"]), 2)
    config["ast_threshold"] = round(float(best["upper_th"]), 2)
    config["winnowing_threshold"] = round(float(best["upper_th"]), 2)

    with open(frozen_config_path, "w", encoding="utf-8") as f:
        json.dump(config, f, indent=2)

    print(f"\n[OK] Hasil tuning parameter Hybrid otomatis diperbarui ke: {frozen_config_path}")
    print(f"     lower_threshold     : {config['lower_threshold']}")
    print(f"     upper_threshold     : {config['upper_threshold']}")
    print(f"     ast_threshold       : {config['ast_threshold']}")
    print(f"     winnowing_threshold : {config['winnowing_threshold']}")


def load_and_precalculate_scores(
    csv_path: str,
    k: int,
    w: int,
    w_struct: float,
    w_fp: float
) -> Tuple[List[Dict[str, Any]], int, int]:
    """
    Membaca dataset dan menghitung skor Winnowing serta skor AST 1x untuk seluruh pasangan,
    sehingga proses Grid Search ambang batas Fast-Exit berjalan instan (in-memory).
    """
    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"Dataset tidak ditemukan: {csv_path}")

    dataset_dir = os.path.dirname(os.path.abspath(csv_path))
    winnowing_engine = StandaloneWinnowingEngine(k=k, w=w)
    ast_engine = StandaloneJavaASTEngine(weight_structural=w_struct, weight_subtree=w_fp)

    raw_cache: Dict[str, str] = {}
    prep_cache: Dict[str, str] = {}
    ast_cache: Dict[str, Tuple[JavaASTNode, set]] = {}

    def get_file_content(rel_path: str) -> str:
        clean_rel = rel_path.strip().lstrip("\\/")
        full_path = os.path.join(dataset_dir, clean_rel)
        if full_path not in raw_cache:
            if not os.path.exists(full_path):
                raise FileNotFoundError(f"File kode tidak ditemukan: {full_path}")
            with open(full_path, "r", encoding="utf-8", errors="ignore") as f:
                raw_cache[full_path] = f.read()
        return raw_cache[full_path]

    def get_winn_fp(code_text: str, key: str):
        if key not in prep_cache:
            prep_cache[key] = winnowing_engine.preprocess(code_text)
        return winnowing_engine.get_fingerprints_from_preprocessed(prep_cache[key])

    def get_ast_repr(code_text: str, key: str) -> Tuple[JavaASTNode, set]:
        if key not in ast_cache:
            raw_ast = ast_engine.parse(code_text)
            can_ast = ast_engine.canonicalize(raw_ast)
            norm_ast = ast_engine.normalize(can_ast)
            fp_set = ast_engine.get_subtree_fingerprints(norm_ast)
            ast_cache[key] = (norm_ast, fp_set)
        return ast_cache[key]

    pairs: List[Dict[str, Any]] = []
    with open(csv_path, mode="r", encoding="utf-8") as f:
        sample = f.readline()
        delimiter = "|" if "|" in sample else ","
        f.seek(0)

        reader = csv.DictReader(f, delimiter=delimiter)
        for row_idx, row in enumerate(reader):
            kode = row.get("kode_plag", "")
            is_plag = read_label(row)

            if "file1" in row and "file2" in row:
                c1 = get_file_content(row["file1"])
                c2 = get_file_content(row["file2"])
                k1 = os.path.join(dataset_dir, row["file1"].strip().lstrip("\\/"))
                k2 = os.path.join(dataset_dir, row["file2"].strip().lstrip("\\/"))
                desc = f"{kode} ({os.path.basename(row['file1'])} vs {os.path.basename(row['file2'])})"
            else:
                c1 = row["code1"]
                c2 = row["code2"]
                k1 = f"inline:{row_idx}:1"
                k2 = f"inline:{row_idx}:2"
                desc = row.get("description", "")

            # 1. Hitung skor Winnowing
            fp1_winn = get_winn_fp(c1, k1)
            fp2_winn = get_winn_fp(c2, k2)
            winn_sim = winnowing_engine.calculate_similarity_from_fingerprints(fp1_winn, fp2_winn)

            # 2. Hitung skor AST
            norm1, fp1_ast = get_ast_repr(c1, k1)
            norm2, fp2_ast = get_ast_repr(c2, k2)
            struct_sim = ast_engine.calculate_structural_similarity(norm1, norm2)

            if not fp1_ast and not fp2_ast:
                fp_sim = 0.0
            else:
                inter = len(fp1_ast.intersection(fp2_ast))
                min_sz = min(len(fp1_ast), len(fp2_ast))
                union = len(fp1_ast.union(fp2_ast))
                containment = inter / min_sz if min_sz > 0 else 0.0
                jaccard = inter / union if union > 0 else 0.0
                fp_sim = 0.50 * containment + 0.50 * jaccard

            ast_sim = (w_struct * struct_sim) + (w_fp * fp_sim)

            pairs.append({
                "id": row.get("no", row.get("id", str(row_idx))),
                "kode_plag": kode,
                "description": desc,
                "label": is_plag,
                "winn_sim": winn_sim,
                "ast_sim": ast_sim
            })

    return pairs, len(raw_cache) or len(pairs) * 2, len(pairs)


def evaluate_hybrid_grid(
    dataset_path: str,
    lower_th_list: List[float],
    upper_th_list: List[float],
    output_summary_csv: str = None,
    output_summary_md: str = None,
    frozen_config_path: str = FROZEN_CONFIG_PATH
) -> Dict[str, Any]:
    # Baca parameter beku Winnowing & AST dari frozen_config.json
    k = 7
    w = 3
    w_struct = 0.60
    w_fp = 0.40

    if frozen_config_path and os.path.exists(frozen_config_path):
        try:
            with open(frozen_config_path, "r", encoding="utf-8") as f:
                cfg = json.load(f)
                k = int(cfg.get("k", k))
                w = int(cfg.get("w", w))
                w_struct = float(cfg.get("weight_structural", w_struct))
                w_fp = float(cfg.get("weight_subtree", w_fp))
        except Exception as e:
            print(f"[WARNING] Gagal membaca konfigurasi beku: {e}, menggunakan default.")

    print("=" * 80)
    print("  OPTIMASI AMBANG BATAS PIPELINE HYBRID (GRID SEARCH lower_th & upper_th)")
    print(f"  Dataset               : {dataset_path}")
    print(f"  Parameter Winnowing   : k={k}, w={w} (Dibekukan dari Tahap 1)")
    print(f"  Bobot Komposit AST    : w_struct={w_struct:.2f}, w_fp={w_fp:.2f} (Dibekukan dari Tahap 2)")
    print(f"  Kandidat lower_th ({len(lower_th_list)} nilai) : {lower_th_list}")
    print(f"  Kandidat upper_th ({len(upper_th_list)} nilai) : {upper_th_list}")
    print("=" * 80)

    print("\n[1/3] Membaca dataset & pra-kalkulasi skor Winnowing serta AST...")
    t_start = time.perf_counter()
    pairs, total_files, total_pairs = load_and_precalculate_scores(dataset_path, k, w, w_struct, w_fp)
    n_plag = sum(1 for p in pairs if p["label"] == 1)
    n_non_plag = total_pairs - n_plag
    elapsed_precalc = time.perf_counter() - t_start
    print(f"      Total data: {total_pairs} pasangan ({n_plag} plagiat, {n_non_plag} non-plagiat)")
    print(f"      Pra-kalkulasi skor selesai dalam {elapsed_precalc:.2f}s")

    print("\n[2/3] Menjalankan evaluasi seluruh kombinasi ambang batas Fast-Exit...")
    print(f"{'No':<4} | {'lower':<6} | {'upper':<6} | {'Bypass (%)':<11} | {'Akurasi (%)':<12} | {'Sim Plag (%)':<13} | {'Sim Non (%)':<12}")
    print("-" * 77)

    grid_results: List[Dict[str, Any]] = []
    comb_idx = 0

    for l_th in lower_th_list:
        for u_th in upper_th_list:
            if l_th >= u_th:
                continue

            comb_idx += 1
            fast_exit_low = 0
            fast_exit_high = 0
            ast_invoked = 0
            correct = 0

            plag_final_scores = []
            non_plag_final_scores = []

            for p in pairs:
                w_sim = p["winn_sim"]
                a_sim = p["ast_sim"]
                label = p["label"]

                if w_sim <= l_th:
                    # Stage 1: Fast-Exit Rendah -> Non-Plagiat (Skip AST)
                    fast_exit_low += 1
                    final_sim = w_sim
                    pred_plag = 0
                elif w_sim >= u_th:
                    # Stage 1: Fast-Exit Tinggi -> Plagiat (Skip AST)
                    fast_exit_high += 1
                    final_sim = w_sim
                    pred_plag = 1
                else:
                    # Stage 2: Zona Abu-abu -> Verifikasi AST
                    ast_invoked += 1
                    final_sim = max(w_sim, a_sim)
                    pred_plag = 1 if final_sim >= u_th else 0

                if pred_plag == label:
                    correct += 1

                if label == 1:
                    plag_final_scores.append(final_sim)
                else:
                    non_plag_final_scores.append(final_sim)

            bypass_count = fast_exit_low + fast_exit_high
            bypass_rate = (bypass_count / total_pairs) * 100.0 if total_pairs > 0 else 0.0
            accuracy = (correct / total_pairs) * 100.0 if total_pairs > 0 else 0.0

            avg_plag = (sum(plag_final_scores) / len(plag_final_scores)) * 100.0 if plag_final_scores else 0.0
            avg_non_plag = (sum(non_plag_final_scores) / len(non_plag_final_scores)) * 100.0 if non_plag_final_scores else 0.0

            entry = {
                "lower_th": round(l_th, 2),
                "upper_th": round(u_th, 2),
                "bypass_rate": round(bypass_rate, 2),
                "avg_plag_similarity": round(avg_plag, 2),
                "avg_non_plag_similarity": round(avg_non_plag, 2),
                # Atribut pendukung internal
                "accuracy": round(accuracy, 2),
                "fast_exit_low": fast_exit_low,
                "fast_exit_high": fast_exit_high,
                "ast_invoked": ast_invoked,
            }
            grid_results.append(entry)

            print(f"{comb_idx:<4} | {l_th:<6.2f} | {u_th:<6.2f} | {bypass_rate:>10.2f}% | {accuracy:>11.2f}% | {avg_plag:>11.2f}% | {avg_non_plag:>11.2f}%")

    if not grid_results:
        raise ValueError("Tidak ada kombinasi parameter yang valid (lower_th < upper_th).")

    # Seleksi parameter terbaik
    best, ranked_all, cands = select_hybrid(grid_results, min_accuracy=100.0)

    print("\n" + "=" * 80)
    print("  HASIL AKHIR: PARAMETER ZONA FAST-EXIT HYBRID OPTIMAL")
    print("=" * 80)
    print("  >> Kriteria Seleksi         : Akurasi 100% & Bypass Rate Tertinggi (Efisiensi Maksimal)")
    print(f"  >> Total kandidat akurat 100% : {len(cands)} dari {len(grid_results)} kombinasi")
    print(f"  >> Fast-Exit Bawah (lower_th) : {best['lower_th']:.2f} ({best['lower_th']*100:.0f}%) -> Skip AST Non-Plagiat")
    print(f"  >> Fast-Exit Atas (upper_th)  : {best['upper_th']:.2f} ({best['upper_th']*100:.0f}%) -> Skip AST Plagiat")
    print(f"  >> Ambang Vonis (tau)         : {best['upper_th']:.2f} ({best['upper_th']*100:.0f}%)")
    print(f"  >> Efisiensi Bypass AST       : {best['bypass_rate']:.2f}% ({best['fast_exit_low'] + best['fast_exit_high']}/{total_pairs} pasangan)")
    print(f"     - Fast-Exit Rendah         : {best['fast_exit_low']} pasangan ({best['fast_exit_low']/total_pairs*100:.2f}%)")
    print(f"     - Fast-Exit Tinggi         : {best['fast_exit_high']} pasangan ({best['fast_exit_high']/total_pairs*100:.2f}%)")
    print(f"     - Verifikasi Mendalam AST  : {best['ast_invoked']} pasangan ({best['ast_invoked']/total_pairs*100:.2f}%)")
    print(f"  >> Rata-rata Kemiripan PLAG   : {best['avg_plag_similarity']:.2f}%")
    print(f"  >> Rata-rata Kemiripan NON    : {best['avg_non_plag_similarity']:.2f}%")
    print("=" * 80)

    print("\nLEADERBOARD 10 KOMBINASI PARAMETER TERATAS:")
    print(f"{'Peringkat':<10} | {'lower':<6} | {'upper':<6} | {'Bypass (%)':<11} | {'Akurasi (%)':<12} | {'Sim Plag (%)':<13} | {'Sim Non (%)':<12}")
    print("-" * 83)
    for rank, res in enumerate(ranked_all[:10], start=1):
        print(f"#{rank:<9} | {res['lower_th']:<6.2f} | {res['upper_th']:<6.2f} | {res['bypass_rate']:>10.2f}% | {res['accuracy']:>11.2f}% | {res['avg_plag_similarity']:>11.2f}% | {res['avg_non_plag_similarity']:>11.2f}%")
    print("=" * 83)

    # Simpan ke CSV 6 kolom: lower_th|upper_th|bypass_rate|accuracy|avg_plag_similarity|avg_non_plag_similarity
    if output_summary_csv:
        os.makedirs(os.path.dirname(os.path.abspath(output_summary_csv)), exist_ok=True)
        fieldnames = ["lower_th", "upper_th", "bypass_rate", "accuracy", "avg_plag_similarity", "avg_non_plag_similarity"]
        with open(output_summary_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames, delimiter="|")
            writer.writeheader()
            for r in ranked_all:
                writer.writerow({
                    "lower_th": r["lower_th"],
                    "upper_th": r["upper_th"],
                    "bypass_rate": r["bypass_rate"],
                    "accuracy": r["accuracy"],
                    "avg_plag_similarity": r["avg_plag_similarity"],
                    "avg_non_plag_similarity": r["avg_non_plag_similarity"],
                })
        print(f"\n[OK] Seluruh hasil tuning parameter Hybrid disimpan ke: {output_summary_csv}")

    # Simpan laporan ringkasan Markdown jika ditentukan
    if output_summary_md:
        generate_tuning_hybrid_md(
            best=best,
            ranked_all=ranked_all,
            total_pairs=total_pairs,
            k=k,
            w=w,
            w_struct=w_struct,
            w_fp=w_fp,
            output_md_path=output_summary_md
        )

    # Update frozen_config.json secara langsung
    if frozen_config_path:
        update_frozen_config(best, frozen_config_path)

    return {
        "best_parameter": best,
        "candidates": cands,
        "grid_results": ranked_all
    }


def generate_tuning_hybrid_md(
    best: Dict[str, Any],
    ranked_all: List[Dict[str, Any]],
    total_pairs: int,
    k: int,
    w: int,
    w_struct: float,
    w_fp: float,
    output_md_path: str
) -> None:
    """
    Menghasilkan dokumentasi markdown lengkap mengenai hasil tuning hybrid:
    - Penjelasan arti bypass_rate dan kolom-kolom lainnya
    - Parameter terpilih beserta rincian pembagian zona
    - Tabel leaderboard 10 konfigurasi teratas
    """
    os.makedirs(os.path.dirname(os.path.abspath(output_md_path)), exist_ok=True)

    md_content = f"""# Laporan Penyetelan Hyperparameter Pipeline Hybrid (Adaptif 3-Zona)

Dokumen ini menyajikan hasil optimasi *grid search* ambang batas keputusan (*Fast-Exit*) pada **Pipeline Hybrid (Winnowing + Java AST)** menggunakan data tuning independen ({total_pairs} pasangan kode).

---

## 1. Definisi & Makna Kolom Evaluasi

| Nama Kolom | Tipe | Definisi & Peran dalam Pipeline | Signifikansi Ilmiah |
| :--- | :---: | :--- | :--- |
| **`lower_th`** | Float | **Ambang Batas Bawah Fast-Exit ($\\theta_{{\\text{{lower}}}}$)**. Jika skor Winnowing $\\le \\theta_{{\\text{{lower}}}}$, pasangan kode langsung diputuskan sebagai **Non-Plagiat** dan eksekusi AST di-skip total. | Menjamin kode yang terbukti berbeda secara leksikal tidak membebani parser AST. |
| **`upper_th`** | Float | **Ambang Batas Atas Fast-Exit ($\\theta_{{\\text{{upper}}}}$)**. Jika skor Winnowing $\\ge \\theta_{{\\text{{upper}}}}$, pasangan kode langsung diputuskan sebagai **Plagiat** dan eksekusi AST di-skip. Nilai ini sekaligus berfungsi sebagai **Ambang Vonis Klasifikasi ($\\tau$)**. | Menjamin kode dengan kemiripan tekstual tinggi (plagiarisme langsung) diputus instan tanpa latensi AST. |
| **`bypass_rate`** | Float (%) | **Rasio Pengabaian Verifikasi AST**. Persentase pasangan kode yang berhasil diselesaikan di Stage 1 (Winnowing) tanpa perlu memanggil analisis AST: <br>$$\\text{{Bypass Rate}} = \\frac{{N_{{\\text{{Fast-Exit Low}}}} + N_{{\\text{{Fast-Exit High}}}}}}{{N_{{\\text{{Total}}}}}} \\times 100\\%$$ | **Metrik efisiensi utama Hybrid**. Mengukur seberapa besar pemangkasan beban komputasi kuadratik AST yang berhasil dicapai oleh sistem. |
| **`accuracy`** | Float (%) | **Akurasi Klasifikasi pada Data Tuning**. Persentase ketepatan keputusan (Plagiat vs Non-Plagiat) terhadap *ground-truth label*: <br>$$\\text{{Accuracy}} = \\frac{{\\text{{TP}} + \\text{{TN}}}}{{N_{{\\text{{Total}}}}}} \\times 100\\%$$ | **Filter Keamanan Mutlak (*Gate Constraint*)**. Hanya konfigurasi dengan akurasi $100{{,}}00\\%$ yang valid sebagai kandidat untuk mencegah *false exit*. |
| **`avg_plag_similarity`** | Float (%) | **Rata-rata Kemiripan Pasangan Plagiat**. Rata-rata skor kemiripan akhir pada seluruh pasangan berlabel plagiat ($N=77$). | Mengukur sensitivitas tangkapan deteksi terhadap berbagai varian obfusikasi plagiarisme. |
| **`avg_non_plag_similarity`** | Float (%) | **Rata-rata Kemiripan Pasangan Non-Plagiat**. Rata-rata skor kemiripan akhir pada seluruh pasangan kontrol berlabel non-plagiat ($N=18$). | Mengukur ketahanan sistem terhadap *false positive* (*noise floor*). Semakin rendah, semakin tegas margin pemisahnya. |

---

## 2. Parameter Optimal Terpilih

Berdasarkan kriteria seleksi ganda: **Akurasi $100{{,}}00\\%$ (Zero False Exit) & Bypass Rate Tertinggi (Efisiensi Maksimal)**, parameter optimal yang terpilih adalah:

### Konfigurasi Terpilih (Peringkat #1)
- **Fast-Exit Bawah (`lower_th`)** : **{best['lower_th']:.2f}** ({best['lower_th']*100:.0f}%)
- **Fast-Exit Atas (`upper_th`)**  : **{best['upper_th']:.2f}** ({best['upper_th']*100:.0f}%)
- **Ambang Vonis Klasifikasi ($\\tau$)** : **{best['upper_th']:.2f}** ({best['upper_th']*100:.0f}%)
- **Tingkat Akurasi** : **{best['accuracy']:.2f}%**
- **Efisiensi Bypass AST** : **{best['bypass_rate']:.2f}%** ({best['fast_exit_low'] + best['fast_exit_high']}/{total_pairs} pasangan dilewati)

### Rincian Distribusi Keputusan 3-Zona
| Zona Keputusan | Interval Ambang Batas | Jumlah Pasangan | Rasio Pasangan (%) | Status Eksekusi AST |
| :--- | :---: | :---: | :---: | :--- |
| **Fast-Exit Rendah** | $\\text{{Sim}}_{{\\text{{Winn}}}} \\le {best['lower_th']:.2f}$ | {best['fast_exit_low']} pasang | {best['fast_exit_low']/total_pairs*100:.2f}% | **Diabaikan (Skip AST)** $\\rightarrow$ Non-Plagiat |
| **Zona Abu-abu (Grey Zone)** | ${best['lower_th']:.2f} < \\text{{Sim}}_{{\\text{{Winn}}}} < {best['upper_th']:.2f}$ | {best['ast_invoked']} pasang | {best['ast_invoked']/total_pairs*100:.2f}% | **Dieksekusi (AST Analisis)** |
| **Fast-Exit Tinggi** | $\\text{{Sim}}_{{\\text{{Winn}}}} \\ge {best['upper_th']:.2f}$ | {best['fast_exit_high']} pasang | {best['fast_exit_high']/total_pairs*100:.2f}% | **Diabaikan (Skip AST)** $\\rightarrow$ Plagiat |

### Rata-rata Kemiripan Akhir
- **Rata-rata Kemiripan Plagiat** : **{best['avg_plag_similarity']:.2f}%**
- **Rata-rata Kemiripan Non-Plagiat** : **{best['avg_non_plag_similarity']:.2f}%**
- **Margin Pemisah ($\\Delta = \\mu_{{\\text{{plag}}}} - \\mu_{{\\text{{non}}}}$)** : **{best['avg_plag_similarity'] - best['avg_non_plag_similarity']:.2f}%**

---

## 3. Leaderboard 10 Kombinasi Parameter Teratas

Berikut adalah 10 kombinasi terbaik yang diurutkan berdasarkan prioritas: **Akurasi $100\\%$ $\\rightarrow$ Bypass Rate Tertinggi $\\rightarrow$ Margin Kemiripan**:

| Peringkat | `lower_th` | `upper_th` | `bypass_rate` (%) | `accuracy` (%) | `avg_plag_similarity` (%) | `avg_non_plag_similarity` (%) |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
"""
    for rank, res in enumerate(ranked_all[:10], start=1):
        md_content += f"| #{rank} | {res['lower_th']:.2f} | {res['upper_th']:.2f} | {res['bypass_rate']:.2f}% | {res['accuracy']:.2f}% | {res['avg_plag_similarity']:.2f}% | {res['avg_non_plag_similarity']:.2f}% |\n"

    md_content += f"""
---

## 4. Konfigurasi Parameter Prasyarat (Frozen Parameters)
Penyetelan ini menggunakan parameter beku yang telah dioptimasi pada tahap sebelumnya:
- **Winnowing Engine**: $k = {k}$, $w = {w}$ (Hasil `tunning_winnowing.py`)
- **Java AST Engine**: $w_{{\\text{{struct}}}} = {w_struct:.2f}$, $w_{{\\text{{fp}}}} = {w_fp:.2f}$ (Hasil `tunning_ast.py`)
"""
    with open(output_md_path, mode="w", encoding="utf-8") as f:
        f.write(md_content)

    print(f"[OK] Laporan markdown ringkasan tuning disimpan ke: {output_md_path}")


if __name__ == "__main__":
    default_dataset = os.path.join(DATASET_DIR, "tunning.csv")
    default_output_csv = os.path.join(RESULTS_DIR, "tunning_res_hybrid.csv")
    default_output_md = os.path.join(RESULTS_DIR, "tunning_res_hybrid.md")

    parser = argparse.ArgumentParser(description="Pencarian Ambang Batas 3-Zona Fast-Exit Optimal untuk Pipeline Hybrid")
    parser.add_argument("--preset", "-p", choices=["quick", "standard", "full"], default="standard",
                        help="Preset kombinasi parameter: quick, standard (default), full")
    parser.add_argument("--lower-values", nargs="+", type=float, default=None,
                        help="Daftar nilai lower_th kustom, contoh: --lower-values 0.10 0.15 0.20")
    parser.add_argument("--upper-values", nargs="+", type=float, default=None,
                        help="Daftar nilai upper_th kustom, contoh: --upper-values 0.65 0.70 0.75")
    parser.add_argument("--dataset", "-d", type=str, default=default_dataset,
                        help="Path ke file dataset CSV penalaan (default: tunning.csv)")
    parser.add_argument("--output", "-o", type=str, default=default_output_csv,
                        help=f"Path file output CSV ringkasan tuning (default: {default_output_csv})")
    parser.add_argument("--output-md", "-m", type=str, default=default_output_md,
                        help=f"Path file output Markdown ringkasan tuning (default: {default_output_md})")
    parser.add_argument("--frozen-config", "-c", type=str, default=FROZEN_CONFIG_PATH,
                        help="Path ke file frozen_config.json untuk diperbarui (default: frozen_config.json)")

    args = parser.parse_args()

    if args.lower_values and args.upper_values:
        lower_list = args.lower_values
        upper_list = args.upper_values
    elif args.preset == "quick":
        lower_list = [0.10, 0.15, 0.20, 0.21]
        upper_list = [0.65, 0.70, 0.75]
    elif args.preset == "full":
        lower_list = [round(x * 0.01, 2) for x in range(5, 31)]
        upper_list = [round(x * 0.01, 2) for x in range(55, 86)]
    else:  # standard
        lower_list = [0.05, 0.08, 0.10, 0.12, 0.15, 0.18, 0.20, 0.21, 0.25]
        upper_list = [0.60, 0.65, 0.70, 0.75, 0.80]

    evaluate_hybrid_grid(
        dataset_path=args.dataset,
        lower_th_list=lower_list,
        upper_th_list=upper_list,
        output_summary_csv=args.output,
        output_summary_md=args.output_md,
        frozen_config_path=args.frozen_config
    )

