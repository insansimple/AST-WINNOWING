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

from java_ast_engine import StandaloneJavaASTEngine, JavaASTNode


def rank_key(r: Dict[str, Any]) -> Tuple:
    """
    Kunci pengurutan tunggal (dipakai untuk CSV, leaderboard, dan pemilihan terbaik):
    avg_plag_similarity paling tinggi, lalu avg_non_plag_similarity paling rendah.
    w_struct hanya sebagai tie-breaker deterministik.
    """
    return (
        -r["avg_plag_similarity"],
        r["avg_non_plag_similarity"],
        r["w_struct"],
    )


def select_best(
    rows: List[Dict[str, Any]],
    max_non_allowed: float = 25.0,
) -> Tuple[Dict[str, Any], List[Dict[str, Any]], List[Dict[str, Any]]]:
    """
    Kriteria pemilihan bobot AST (w_struct dan w_fp):
      1. Batas aman kemiripan non-plagiat: avg_non_plag_similarity <= max_non_allowed (25.0%)
         (jika tidak ada yang lolos, seluruh kombinasi dipakai)
      2. Dari kandidat yang lolos, pilih avg_plag_similarity paling tinggi
         dan avg_non_plag_similarity paling rendah.

    Mengembalikan (best, ranked_all, cands):
      - best       : bobot terpilih (= baris pertama ranked_all)
      - ranked_all : seluruh kombinasi; kandidat lolos filter diurutkan lebih dulu
                     (rank_key), disusul kombinasi yang tidak lolos (rank_key juga)
      - cands      : kandidat yang lolos filter
    """
    cands = [r for r in rows if r["avg_non_plag_similarity"] <= max_non_allowed] or rows
    cand_ids = {id(r) for r in cands}
    rest = [r for r in rows if id(r) not in cand_ids]
    ranked_all = sorted(cands, key=rank_key) + sorted(rest, key=rank_key)
    return ranked_all[0], ranked_all, cands


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
    frozen_config_path: str = FROZEN_CONFIG_PATH
) -> Dict[str, Any]:
    print("=" * 80)
    print("  OPTIMASI HYPERPARAMETER BOBOT ENGINE JAVA AST (GRID SEARCH w_struct & w_fp)")
    print(f"  Dataset : {dataset_path}")
    print("=" * 80)

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

    print("\n[2/3] Menjalankan evaluasi bobot kombinasi (21 kombinasi: w_struct 0.00 .. 1.00)...")
    print(f"{'w_struct':>8} | {'w_fp':>6} | {'Sim Plag (%)':>13} | {'Sim Non-Plag (%)':>17}")
    print("-" * 55)

    results: List[Dict[str, Any]] = []
    for w_int in range(0, 105, 5):
        w_s = round(w_int / 100.0, 2)
        w_f = round(1.0 - w_s, 2)

        plag_scores = [w_s * d["struct"] + w_f * d["fp"] for d in plag]
        non_scores = [w_s * d["struct"] + w_f * d["fp"] for d in non_plag]

        avg_plag = (sum(plag_scores) / len(plag_scores)) * 100.0 if plag_scores else 0.0
        avg_non_plag = (sum(non_scores) / len(non_scores)) * 100.0 if non_scores else 0.0

        # Entry hanya berisi kolom yang disimpan ke CSV
        entry = {
            "w_struct": w_s,
            "w_fp": w_f,
            "avg_plag_similarity": round(avg_plag, 2),
            "avg_non_plag_similarity": round(avg_non_plag, 2),
        }
        results.append(entry)

        print(f"{w_s:8.2f} | {w_f:6.2f} | {avg_plag:>11.2f}% | {avg_non_plag:>15.2f}%")

    # Seleksi bobot terbaik + urutan tunggal untuk leaderboard & CSV
    best, ranked_all, candidates = select_best(results, max_non_allowed=25.0)

    print("\n" + "=" * 80)
    print("  HASIL AKHIR: BOBOT KOMPONEN AST OPTIMAL / TERBAIK")
    print("=" * 80)
    print("  >> Kriteria Seleksi   : avg_plag_similarity Paling Tinggi & avg_non_plag_similarity Paling Rendah")
    print(f"  >> Total kandidat lolos batas non-plagiat (<= 25.0%) : {len(candidates)} kombinasi")
    print(f"  >> Bobot Struktural (w_struct)  : {best['w_struct']:.2f} ({best['w_struct']*100:.0f}%)")
    print(f"  >> Bobot Fingerprint (w_fp)     : {best['w_fp']:.2f} ({best['w_fp']*100:.0f}%)")
    print(f"  >> Rata-rata Kemiripan PLAGIAT  : {best['avg_plag_similarity']:.2f}%")
    print(f"  >> Rata-rata Kemiripan NON-PLAG : {best['avg_non_plag_similarity']:.2f}%")
    print("=" * 80)

    print("\nLEADERBOARD 10 KOMBINASI BOBOT TERATAS:")
    print(f"{'Peringkat':<10} | {'w_struct':>8} | {'w_fp':>6} | {'Sim Plag (%)':>13} | {'Sim Non-Plag (%)':>17}")
    print("-" * 66)
    for rank, res in enumerate(ranked_all[:10], start=1):
        print(f"#{rank:<9} | {res['w_struct']:8.2f} | {res['w_fp']:6.2f} | {res['avg_plag_similarity']:>11.2f}% | {res['avg_non_plag_similarity']:>15.2f}%")
    print("=" * 66)

    # Breakdown per kode plagiarisme
    print(f"\n[3/3] Breakdown Kemiripan per Kategori Plagiarisme pada Bobot Optimal ({best['w_struct']:.2f}/{best['w_fp']:.2f}):")
    breakdown: Dict[str, List[float]] = {}
    for d in data:
        k = d["kode_plag"]
        score = best["w_struct"] * d["struct"] + best["w_fp"] * d["fp"]
        if k not in breakdown:
            breakdown[k] = []
        breakdown[k].append(score * 100.0)

    print(f"{'Kategori / Kode Plag':<22} | {'Jumlah':<7} | {'Rata-rata Kemiripan (%)':<24}")
    print("-" * 60)
    for k_code, vals in sorted(breakdown.items()):
        avg_val = sum(vals) / len(vals)
        print(f"{k_code:<22} | {len(vals):<7} | {avg_val:>21.2f}%")
    print("=" * 60)

    # Simpan ke CSV: kolom w_struct|w_fp|avg_plag_similarity|avg_non_plag_similarity
    # (urutan = ranked_all dari select_best)
    if output_summary_csv:
        os.makedirs(os.path.dirname(os.path.abspath(output_summary_csv)), exist_ok=True)
        fieldnames = ["w_struct", "w_fp", "avg_plag_similarity", "avg_non_plag_similarity"]
        with open(output_summary_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames, delimiter="|")
            writer.writeheader()
            for r in ranked_all:
                writer.writerow({
                    "w_struct": r["w_struct"],
                    "w_fp": r["w_fp"],
                    "avg_plag_similarity": r["avg_plag_similarity"],
                    "avg_non_plag_similarity": r["avg_non_plag_similarity"],
                })
        print(f"\n[OK] Seluruh hasil tuning parameter disimpan ke: {output_summary_csv}")

    # Update frozen_config.json secara langsung
    if frozen_config_path:
        update_frozen_config(best, frozen_config_path)

    return {
        "best_weights": best,
        "candidates": candidates,
        "grid_results": ranked_all,
        "breakdown": {k: sum(v) / len(v) for k, v in breakdown.items()}
    }


if __name__ == "__main__":
    default_dataset = os.path.join(DATASET_DIR, "tunning.csv")
    default_output = os.path.join(RESULTS_DIR, "tunning_res_ast.csv")

    parser = argparse.ArgumentParser(description="Pencarian Bobot Kombinasi AST Optimal (Structural vs Subtree Fingerprint)")
    parser.add_argument("--dataset", "-d", type=str, default=default_dataset,
                        help="Path ke dataset CSV (default: tunning.csv)")
    parser.add_argument("--output", "-o", type=str, default=default_output,
                        help=f"Path output CSV bobot tuning (default: {default_output})")
    parser.add_argument("--frozen-config", "-c", type=str, default=FROZEN_CONFIG_PATH,
                        help="Path ke file frozen_config.json untuk diperbarui (default: frozen_config.json)")

    args = parser.parse_args()

    tune_ast_weights(
        dataset_path=args.dataset,
        output_summary_csv=args.output,
        frozen_config_path=args.frozen_config
    )
