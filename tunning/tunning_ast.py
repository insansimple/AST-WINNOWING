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

if ENGINE_DIR not in sys.path:
    sys.path.insert(0, ENGINE_DIR)

from dataset_utils import read_label

from java_ast_engine import StandaloneJavaASTEngine, JavaASTNode

def select_best(
    rows: List[Dict[str, Any]],
    tau: float = 0.65,
    min_gap: float = 0.05,
    f1_tol: float = 1e-6,
    require_separable: bool = False,
) -> Tuple[Any, List[Dict[str, Any]], List[Dict[str, Any]]]:
    """
    Seleksi bertahap (lexicographic):
      1. Syarat keras : FP == 0
      2. Syarat keras : tau - max_non >= min_gap
      3. (opsional)   : sep_gap > 0 (ada ambang yang memisahkan kelas sempurna)
      4. F1 tertinggi (selisih <= f1_tol dianggap seri)
      5. Tie-break    : gap terbesar, margin terbesar, AUC terbesar, w_struct terkecil
 
    Fungsi ini tidak memodifikasi `rows`. Mengembalikan (best, tied, candidates);
    jika tidak ada kandidat: (None, [], []).
    """
    def gap(r):
        return tau - r["max_non"]
 
    candidates = [r for r in rows if r["fp"] == 0 and gap(r) >= min_gap]
    if require_separable:
        candidates = [r for r in candidates if r["sep_gap"] > 0]
    if not candidates:
        return None, [], []
 
    best_f1 = max(r["f1"] for r in candidates)
    tied = [r for r in candidates if best_f1 - r["f1"] <= f1_tol]
    tied = sorted(
        tied,
        key=lambda r: (
            -gap(r),          # jarak aman terbesar
            -r["margin"],     # margin terbesar
            -r["auc_roc"],    # AUC terbesar (presisi penuh, tidak dibulatkan)
            r["w_struct"],    # w_struct terkecil
        ),
    )
    return tied[0], tied, candidates


def update_frozen_config(best: Dict[str, Any], frozen_config_path: str = FROZEN_CONFIG_PATH) -> None:
    """
    Memperbarui parameter bobot AST (weight_structural & weight_subtree)
    langsung ke frozen_config.json dengan tetap mempertahankan konfigurasi lainnya.
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

    config["weight_structural"] = round(float(best["w_struct"]), 2)
    config["weight_subtree"] = round(float(best["w_fp"]), 2)

    with open(frozen_config_path, "w", encoding="utf-8") as f:
        json.dump(config, f, indent=2)

    print(f"\n[OK] Hasil tuning bobot AST otomatis diperbarui ke: {frozen_config_path}")
    print(f"     weight_structural : {config['weight_structural']}")
    print(f"     weight_subtree    : {config['weight_subtree']}")



def compute_auc_roc(pos_scores: List[float], neg_scores: List[float]) -> float:
    """
    Menghitung Area Under the ROC Curve (AUC-ROC) secara eksak menggunakan Mann-Whitney U statistic.
    Nilai 1.0 = pemisahan sempurna antara plagiat dan non-plagiat.
    Nilai 0.5 = acak (random guessing).
    """
    n_pos = len(pos_scores)
    n_neg = len(neg_scores)
    if n_pos == 0 or n_neg == 0:
        return 0.5

    combined = [(s, 1) for s in pos_scores] + [(s, 0) for s in neg_scores]
    combined.sort(key=lambda x: x[0])

    rank_sum_pos = 0.0
    i = 0
    n_total = len(combined)
    while i < n_total:
        j = i
        while j < n_total - 1 and combined[j][0] == combined[j + 1][0]:
            j += 1
        avg_rank = (i + 1 + j + 1) / 2.0
        for k in range(i, j + 1):
            if combined[k][1] == 1:
                rank_sum_pos += avg_rank
        i = j + 1

    u_pos = rank_sum_pos - (n_pos * (n_pos + 1)) / 2.0
    return u_pos / (n_pos * n_neg)


def load_and_extract_ast_features(csv_path: str) -> Tuple[List[Dict[str, Any]], Dict[str, Tuple[JavaASTNode, set]]]:
    """
    Membaca dataset, mem-parse AST 1x untuk setiap file unik,
    dan menghitung structural_sim serta fingerprint_sim untuk seluruh pasangan secara in-memory.
    """
    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"Dataset tidak ditemukan: {csv_path}")

    dataset_dir = os.path.dirname(os.path.abspath(csv_path))
    engine = StandaloneJavaASTEngine()

    raw_cache: Dict[str, str] = {}
    ast_cache: Dict[str, Tuple[JavaASTNode, set]] = {}

    def get_ast(rel_path: str) -> Tuple[JavaASTNode, set]:
        clean_rel = rel_path.strip().lstrip("\\/")
        full_path = os.path.join(dataset_dir, clean_rel)
        if full_path not in ast_cache:
            if not os.path.exists(full_path):
                raise FileNotFoundError(f"File kode tidak ditemukan: {full_path}")
            with open(full_path, "r", encoding="utf-8", errors="ignore") as f:
                code_text = f.read()
            raw_cache[full_path] = code_text

            raw_ast = engine.parse(code_text)
            can_ast = engine.canonicalize(raw_ast)
            norm_ast = engine.normalize(can_ast)
            fp_set = engine.get_subtree_fingerprints(norm_ast)
            ast_cache[full_path] = (norm_ast, fp_set)
        return ast_cache[full_path]

    rows_data: List[Dict[str, Any]] = []
    with open(csv_path, mode="r", encoding="utf-8") as f:
        sample = f.readline()
        delimiter = "|" if "|" in sample else ","
        f.seek(0)

        reader = csv.DictReader(f, delimiter=delimiter)
        for row in reader:
            kode = row.get("kode_plag", "")
            is_plag = read_label(row)

            if "file1" in row and "file2" in row:
                norm1, fp1 = get_ast(row["file1"])
                norm2, fp2 = get_ast(row["file2"])
                desc = f"{kode} ({os.path.basename(row['file1'])} vs {os.path.basename(row['file2'])})"
            else:
                c1 = row["code1"]
                c2 = row["code2"]
                raw1 = engine.parse(c1)
                can1 = engine.canonicalize(raw1)
                norm1 = engine.normalize(can1)
                fp1 = engine.get_subtree_fingerprints(norm1)

                raw2 = engine.parse(c2)
                can2 = engine.canonicalize(raw2)
                norm2 = engine.normalize(can2)
                fp2 = engine.get_subtree_fingerprints(norm2)
                desc = row.get("description", "")

            # Structural similarity
            struct_sim = engine.calculate_structural_similarity(norm1, norm2)

            # Fingerprint similarity (50% containment + 50% jaccard)
            if not fp1 and not fp2:
                fp_sim = 0.0
            else:
                inter = len(fp1.intersection(fp2))
                min_sz = min(len(fp1), len(fp2))
                union = len(fp1.union(fp2))
                containment = inter / min_sz if min_sz > 0 else 0.0
                jaccard = inter / union if union > 0 else 0.0
                fp_sim = 0.50 * containment + 0.50 * jaccard

            rows_data.append({
                "id": row.get("no", row.get("id", "")),
                "kode_plag": kode,
                "description": desc,
                "label": is_plag,
                "struct": struct_sim,
                "fp": fp_sim
            })

    return rows_data, ast_cache


def tune_ast_weights(
    dataset_path: str,
    output_summary_csv: str = None,
    tau_ref: float = StandaloneJavaASTEngine.DEFAULT_THRESHOLD,
    frozen_config_path: str = FROZEN_CONFIG_PATH
) -> Dict[str, Any]:
    print("=" * 115)
    print("  OPTIMASI HYPERPARAMETER BOBOT ENGINE JAVA AST (GRID SEARCH w_struct & w_fp)")
    print(f"  Dataset               : {dataset_path}")
    print(f"  Threshold Acuan (tau) : {tau_ref:.2f}")
    print("=" * 115)

    print("\n[1/3] Membaca dataset & ekstraksi fitur AST (Tree Normalization + Subtree Fingerprints)...")
    t_start = time.perf_counter()
    data, ast_cache = load_and_extract_ast_features(dataset_path)
    total_samples = len(data)
    if not total_samples:
        raise ValueError("Dataset tuning kosong")
    plag = [d for d in data if d["label"] == 1]
    non_plag = [d for d in data if d["label"] == 0]
    elapsed_extract = time.perf_counter() - t_start
    print(f"      Total data: {total_samples} pasangan ({len(plag)} plagiat, {len(non_plag)} non-plagiat)")
    print(f"      File AST unik di-cache: {len(ast_cache)} file (selesai dalam {elapsed_extract:.2f}s)")

    print(f"\n[2/3] Menjalankan evaluasi bobot kombinasi (21 kombinasi: w_struct 0.00 .. 1.00)...")
    print(f"{'w_struct':>8} | {'w_fp':>6} | {'mu_plag':>8} | {'mu_non':>8} | {'Margin':>8} | {'Acc (%)':>8} | {'Prec (%)':>8} | {'Rec (%)':>8} | {'F1 (%)':>8} | {'FP':>4} | {'FN':>4} | {'Max Non':>8} | {'AUC-ROC':>8}")
    print("-" * 125)

    results = []
    for w_int in range(0, 105, 5):
        w_s = round(w_int / 100.0, 2)
        w_f = round(1.0 - w_s, 2)

        plag_scores = [w_s * d["struct"] + w_f * d["fp"] for d in plag]
        non_scores = [w_s * d["struct"] + w_f * d["fp"] for d in non_plag]

        mu_p = sum(plag_scores) / len(plag_scores) if plag_scores else 0.0
        mu_n = sum(non_scores) / len(non_scores) if non_scores else 0.0
        margin = mu_p - mu_n
        max_non = max(non_scores) if non_scores else 0.0
        auc = compute_auc_roc(plag_scores, non_scores)

        # Klasifikasi pada ambang batas tau_ref (default 0.65)
        tp = sum(1 for s in plag_scores if s >= tau_ref)
        fn = sum(1 for s in plag_scores if s < tau_ref)
        fp = sum(1 for s in non_scores if s >= tau_ref)
        tn = sum(1 for s in non_scores if s < tau_ref)

        acc = (tp + tn) / total_samples * 100.0 if total_samples > 0 else 0.0
        prec = (tp / (tp + fp) * 100.0) if (tp + fp) > 0 else 0.0
        rec = (tp / (tp + fn) * 100.0) if (tp + fn) > 0 else 0.0
        f1 = (2 * prec * rec / (prec + rec)) if (prec + rec) > 0 else 0.0

        entry = {
            "w_struct": w_s,
            "w_fp": w_f,
            "mu_plag": mu_p,
            "mu_non": mu_n,
            "margin": margin,
            "acc": acc,
            "prec": prec,
            "rec": rec,
            "f1": f1,
            "fp": fp,
            "fn": fn,
            "max_non": max_non,
            "auc_roc": auc
        }
        results.append(entry)

        print(f"{w_s:8.2f} | {w_f:6.2f} | {mu_p*100:7.2f}% | {mu_n*100:7.2f}% | {margin*100:7.2f}% | {acc:7.2f}% | {prec:7.2f}% | {rec:7.2f}% | {f1:7.2f}% | {fp:4d} | {fn:4d} | {max_non*100:7.2f}% | {auc:8.4f}")

    # Seleksi kombinasi terbaik menggunakan logika multi-kriteria:
    # 1. Safety gap = tau - max_non
    # 2. Syarat keras: FP == 0 dan gap >= min_gap (0.05)
    # 3. F1-score tertinggi (toleransi f1_tol dianggap seri)
    # 4-7. Tie-break berurutan: -gap, -margin, -auc_roc, w_struct
    best, tied, candidates = select_best(results, tau=tau_ref, min_gap=0.05, f1_tol=1e-6)
    if best is None:
        print("\n[WARNING] Tidak ada kandidat yang memenuhi syarat keras (FP=0 dan gap >= 0.05). Menggunakan fallback.")
        zero_fp = [r for r in results if r["fp"] == 0]
        pool = zero_fp if zero_fp else results
        sorted_pool = sorted(
            pool,
            key=lambda x: (
                x["max_non"],       # max_non terkecil sebagai kriteria pertama
                -x["f1"],           # F1 tertinggi
                -x["acc"],          # Akurasi tertinggi
                -x["margin"],       # Margin terbesar
                -x["auc_roc"],      # AUC terbesar
                x["w_struct"],      # w_struct terkecil
            )
        )
        best = sorted_pool[0]
        tied = [best]
        candidates = sorted_pool

    print("\n" + "=" * 80)
    print("  HASIL AKHIR: BOBOT KOMPONEN AST OPTIMAL / TERBAIK")
    print("=" * 80)
    print(f"  >> Total kandidat lolos syarat keras (FP=0, gap>=0.05) : {len(candidates)} kombinasi")
    print(f"  >> Jumlah kandidat seri F1 tertinggi                   : {len(tied)} kombinasi")
    print(f"  >> Bobot Struktural (w_struct)  : {best['w_struct']:.2f} ({best['w_struct']*100:.0f}%)")
    print(f"  >> Bobot Fingerprint (w_fp)    : {best['w_fp']:.2f} ({best['w_fp']*100:.0f}%)")
    print(f"  >> Rata-rata Kemiripan PLAGIAT : {best['mu_plag']*100:.2f}%")
    print(f"  >> Rata-rata Kemiripan NON-PLAG: {best['mu_non']*100:.2f}%")
    print(f"  >> Margin Pemisah (PLAG - NON) : {best['margin']*100:.2f}%")
    print(f"  >> Akurasi pada tau={tau_ref}        : {best['acc']:.2f}%")
    print(f"  >> Presisi                     : {best['prec']:.2f}% (FP = {best['fp']})")
    print(f"  >> Recall                      : {best['rec']:.2f}% (FN = {best['fn']})")
    print(f"  >> F1-Score                    : {best['f1']:.2f}%")
    print(f"  >> Max Non-Plagiat Similarity  : {best['max_non']*100:.2f}%")
    print(f"  >> Safety Gap (tau - max_non)  : {best.get('gap', tau_ref - best['max_non'])*100:.2f}%")
    print(f"  >> AUC-ROC Score               : {best['auc_roc']:.4f}")
    print("=" * 80)

    # Analisis Sensitivitas Threshold pada Bobot Terbaik
    print(f"\n[3/3] Analisis Sensitivitas Threshold (tau) pada Bobot Terbaik (w_struct={best['w_struct']:.2f}):")
    print(f"{'Threshold (tau)':>15} | {'FP':>6} | {'FN':>6} | {'Akurasi (%)':>12} | {'Presisi (%)':>12} | {'F1-Score (%)':>13}")
    print("-" * 75)
    best_plag = [best["w_struct"] * d["struct"] + best["w_fp"] * d["fp"] for d in plag]
    best_non = [best["w_struct"] * d["struct"] + best["w_fp"] * d["fp"] for d in non_plag]

    for tau in [0.50, 0.55, 0.58, 0.60, 0.62, 0.64, 0.65, 0.70]:
        t_fp = sum(1 for s in best_non if s >= tau)
        t_fn = sum(1 for s in best_plag if s < tau)
        t_tp = len(plag) - t_fn
        t_tn = len(non_plag) - t_fp
        t_acc = (t_tp + t_tn) / total_samples * 100.0
        t_prec = (t_tp / (t_tp + t_fp) * 100.0) if (t_tp + t_fp) > 0 else 0.0
        t_rec = (t_tp / (t_tp + t_fn) * 100.0) if (t_tp + t_fn) > 0 else 0.0
        t_f1 = (2 * t_prec * t_rec / (t_prec + t_rec)) if (t_prec + t_rec) > 0 else 0.0
        print(f"{tau:15.2f} | {t_fp:6d} | {t_fn:6d} | {t_acc:11.2f}% | {t_prec:11.2f}% | {t_f1:12.2f}%")
    print("=" * 75)

    # Breakdown per kode plagiarisme
    breakdown = {}
    for d in data:
        k = d["kode_plag"]
        score = best["w_struct"] * d["struct"] + best["w_fp"] * d["fp"]
        if k not in breakdown:
            breakdown[k] = []
        breakdown[k].append(score * 100.0)

    print(f"\nBreakdown Kemiripan per Kategori Plagiarisme pada Bobot Optimal ({best['w_struct']:.2f}/{best['w_fp']:.2f}):")
    print(f"{'Kategori / Kode Plag':<22} | {'Jumlah':<7} | {'Rata-rata Kemiripan (%)':<24}")
    print("-" * 60)
    for k_code, vals in sorted(breakdown.items()):
        avg_val = sum(vals) / len(vals)
        print(f"{k_code:<22} | {len(vals):<7} | {avg_val:>21.2f}%")
    print("=" * 60)

    # Simpan hasil tuning ke CSV
    if output_summary_csv:
        fieldnames = ["w_struct", "w_fp", "mu_plag", "mu_non", "margin", "acc", "prec", "rec", "f1", "fp", "fn", "max_non", "gap", "auc_roc"]
        with open(output_summary_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
            writer.writeheader()
            for r in results:
                row_dict = dict(r)
                row_dict["auc_roc"] = round(row_dict["auc_roc"], 4)
                writer.writerow(row_dict)
        print(f"\n[OK] Seluruh hasil tuning parameter disimpan ke: {output_summary_csv}")

    # Update frozen_config.json secara langsung
    if frozen_config_path:
        update_frozen_config(best, frozen_config_path)

    return {
        "best_weights": best,
        "tied_weights": tied,
        "candidates": candidates,
        "grid_results": results,
        "breakdown": {k: sum(v)/len(v) for k, v in breakdown.items()}
    }


if __name__ == "__main__":
    default_dataset = os.path.join(DATASET_DIR, "tunning.csv")
    default_output = os.path.join(CURRENT_DIR, "tunning_res_ast.csv")

    parser = argparse.ArgumentParser(description="Pencarian Bobot Kombinasi AST Optimal (Structural vs Subtree Fingerprint)")
    parser.add_argument("--dataset", "-d", type=str, default=default_dataset,
                        help="Path ke dataset CSV (default: tunning.csv)")
    parser.add_argument("--threshold", "-t", type=float, default=StandaloneJavaASTEngine.DEFAULT_THRESHOLD,
                        help="Threshold acuan klasifikasi plagiat (default: 0.65)")
    parser.add_argument("--output", "-o", type=str, default=default_output,
                        help="Path output CSV bobot tuning (default: tunning_res_ast.csv)")
    parser.add_argument("--frozen-config", "-c", type=str, default=FROZEN_CONFIG_PATH,
                        help="Path ke file frozen_config.json untuk diperbarui (default: frozen_config.json)")

    args = parser.parse_args()

    tune_ast_weights(
        dataset_path=args.dataset,
        output_summary_csv=args.output,
        tau_ref=args.threshold,
        frozen_config_path=args.frozen_config
    )
