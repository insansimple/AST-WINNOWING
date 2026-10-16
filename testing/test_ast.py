import argparse
import csv
import fnmatch
import json
import os
import sys
import time
from typing import List, Dict, Any

# Direktori dasar
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
PUBLIKASI_DIR = os.path.dirname(CURRENT_DIR)
ENGINE_DIR = os.path.join(PUBLIKASI_DIR, "engine")
DATASET_DIR = os.path.join(PUBLIKASI_DIR, "dataset")

if ENGINE_DIR not in sys.path:
    sys.path.insert(0, ENGINE_DIR)

from dataset_utils import read_label, classification_metrics

from java_ast_engine import StandaloneJavaASTEngine

# ==============================================================================
# PENGATURAN PARAMETER DARI FROZEN CONFIG
# ==============================================================================
FROZEN_CONFIG_PATH = os.path.join(PUBLIKASI_DIR, "frozen_config.json")
CONFIG = {}
if os.path.exists(FROZEN_CONFIG_PATH):
    with open(FROZEN_CONFIG_PATH, "r", encoding="utf-8") as f:
        CONFIG = json.load(f)

THRESHOLD = float(CONFIG.get("ast_threshold", StandaloneJavaASTEngine.DEFAULT_THRESHOLD))
WEIGHT_STRUCTURAL = float(CONFIG.get("weight_structural", StandaloneJavaASTEngine.DEFAULT_WEIGHT_STRUCTURAL))
WEIGHT_SUBTREE = float(CONFIG.get("weight_subtree", StandaloneJavaASTEngine.DEFAULT_WEIGHT_SUBTREE))
FILTER_KODE_PLAG = "ALL"            # Filter pengujian: "ALL" untuk seluruh sampel, atau pola (misal: "PLAG_L1*")

# Konfigurasi Path Berkas Dataset dan Output
DATASET_PATH = os.path.join(DATASET_DIR, "test.csv")
OUTPUT_PATH = os.path.join(CURRENT_DIR, "test_ast_res.csv")
# ==============================================================================


def load_dataset(csv_path: str = DATASET_PATH, kode_plag_filter: str = FILTER_KODE_PLAG) -> List[Dict[str, Any]]:
    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"Dataset file tidak ditemukan: {csv_path}")

    dataset_dir = os.path.dirname(os.path.abspath(csv_path))
    dataset = []
    code_cache = {}

    def read_code_file(rel_path: str) -> str:
        clean_rel = rel_path.strip().lstrip("\\/")
        full_path = os.path.join(dataset_dir, clean_rel)
        if full_path not in code_cache:
            if not os.path.exists(full_path):
                raise FileNotFoundError(f"File kode tidak ditemukan: {full_path}")
            with open(full_path, "r", encoding="utf-8", errors="ignore") as f:
                code_cache[full_path] = f.read()
        return code_cache[full_path]

    with open(csv_path, mode="r", encoding="utf-8") as f:
        sample_line = f.readline()
        delimiter = "|" if "|" in sample_line else ","
        f.seek(0)

        reader = csv.DictReader(f, delimiter=delimiter)
        for row in reader:
            kode = row.get("kode_plag", "")
            if kode_plag_filter and kode_plag_filter.upper() != "ALL":
                if not fnmatch.fnmatch(kode, kode_plag_filter):
                    continue

            if "file1" in row and "file2" in row:
                c1 = read_code_file(row["file1"])
                c2 = read_code_file(row["file2"])
                dataset.append({
                    "id": row.get("no", row.get("id", "")),
                    "kode_plag": kode,
                    "description": f"{kode} ({os.path.basename(row['file1'])} vs {os.path.basename(row['file2'])})",
                    "file1": row["file1"],
                    "file2": row["file2"],
                    "code1": c1,
                    "code2": c2,
                    "label": read_label(row)
                })
            else:
                dataset.append({
                    "id": row.get("id", row.get("no", "")),
                    "kode_plag": kode,
                    "description": row.get("description", ""),
                    "code1": row["code1"],
                    "code2": row["code2"],
                    "label": read_label(row)
                })
    return dataset


def evaluate_ast(
    dataset_path: str = DATASET_PATH,
    threshold: float = THRESHOLD,
    weight_structural: float = WEIGHT_STRUCTURAL,
    weight_subtree: float = WEIGHT_SUBTREE,
    output_result_csv: str = OUTPUT_PATH,
    kode_plag_filter: str = FILTER_KODE_PLAG
) -> Dict[str, Any]:
    print("=" * 75)
    print("  UJICOBA DETEKSI PLAGIARISME JAVA: ENGINE AST SAJA")
    print(f"  Dataset          : {dataset_path}")
    print(f"  Ambang Batas     : threshold = {threshold:.2f}")
    print(f"  Komposisi Bobot  : {weight_structural*100:.0f}% Structural + {weight_subtree*100:.0f}% Subtree Fingerprint")
    if kode_plag_filter and kode_plag_filter.upper() != "ALL":
        print(f"  Filter Kode Plag : {kode_plag_filter}")
    print("=" * 75)

    dataset = load_dataset(dataset_path, kode_plag_filter=kode_plag_filter)
    engine = StandaloneJavaASTEngine(weight_structural=weight_structural, weight_subtree=weight_subtree)

    results = []
    total_time = 0.0

    print(f"{'ID':<12} | {'Label':<6} | {'Sim':<8} | {'Waktu (ms)':<10}")
    print("-" * 50)

    for item in dataset:
        start_t = time.perf_counter()
        det_result = engine.detect(item["code1"], item["code2"], threshold=threshold)
        elapsed_ms = (time.perf_counter() - start_t) * 1000.0
        total_time += elapsed_ms

        struct_sim = det_result["structural_similarity"]
        fp_sim = det_result["fingerprint_similarity"]
        # Hitung skor kemiripan berdasarkan pembobotan yang dikonfigurasi
        sim = det_result["similarity"]
        actual = item["label"]

        results.append({
            "id": item["id"],
            "kode_plag": item.get("kode_plag", ""),
            "description": item["description"],
            "actual_label": actual,
            "predicted_label": int(det_result["is_plagiarized"]),
            "threshold": threshold,
            "similarity": round(sim, 4),
            "structural_sim": round(struct_sim, 4),
            "fingerprint_sim": round(fp_sim, 4),
            "execution_time_ms": round(elapsed_ms, 3),
            "ast1_size": det_result["ast1_size"],
            "ast2_size": det_result["ast2_size"]
        })

        print(f"{item['id']:<12} | {actual:<6} | {sim*100:>6.2f}% | {elapsed_ms:>8.2f} ms")

    metrics = classification_metrics(results)
    print(f"Metrik klasifikasi (%): {metrics}")
    total_samples = len(dataset)
    avg_sim = (sum(r["similarity"] for r in results) / total_samples) if total_samples > 0 else 0.0
    avg_time = total_time / total_samples if total_samples > 0 else 0.0

    print("-" * 60)
    print("HASIL EVALUASI (RATA-RATA):")
    print(f"Total Sampel       : {total_samples}")
    print(f"Rata-rata Kemiripan: {avg_sim * 100:.2f}%")
    print(f"Rata-rata Waktu    : {avg_time:.3f} ms / pasangan kode")
    print("=" * 60)

    # Breakdown per kode_plag
    breakdown = {}
    for r in results:
        k = r["kode_plag"]
        if k not in breakdown:
            breakdown[k] = {"total": 0, "sims": [], "times": []}
        breakdown[k]["total"] += 1
        breakdown[k]["sims"].append(r["similarity"])
        breakdown[k]["times"].append(r["execution_time_ms"])

    if len(breakdown) > 1:
        print("\nBREAKDOWN RATA-RATA PER KODE PLAGIARISME:")
        print(f"{'Kode Plag':<15} | {'Total':<6} | {'Rata Sim':<12} | {'Rata Waktu':<14}")
        print("-" * 55)
        for k, stats in sorted(breakdown.items()):
            tot = stats["total"]
            avg_sim_k = (sum(stats["sims"]) / tot) * 100 if tot > 0 else 0.0
            avg_t_k = sum(stats["times"]) / tot if tot > 0 else 0.0
            print(f"{k:<15} | {tot:<6} | {avg_sim_k:>10.2f}% | {avg_t_k:>10.2f} ms")
        print("=" * 55)

    if output_result_csv:
        fieldnames = [
            "id", "kode_plag", "description", "actual_label", "predicted_label", "threshold", "similarity",
            "structural_sim", "fingerprint_sim",
            "execution_time_ms", "ast1_size", "ast2_size"
        ]
        with open(output_result_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            for r in results:
                writer.writerow(r)
        print(f"[OK] Hasil detail disimpan ke: {output_result_csv}")

    return {
        "engine": "AST",
        "total_samples": total_samples,
        "avg_similarity": avg_sim,
        "avg_time_ms": avg_time,
        "breakdown": breakdown,
        "metrics": metrics,
        "details": results
    }


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Ujicoba Deteksi Plagiarisme Java: Engine AST")
    parser.add_argument("--filter", "-f", type=str, default=FILTER_KODE_PLAG,
                        help=f"Filter kode_plag pattern (default: {FILTER_KODE_PLAG})")
    parser.add_argument("--threshold", "-t", type=float, default=THRESHOLD,
                        help=f"Threshold kemiripan (default: {THRESHOLD})")
    parser.add_argument("--w-struct", "-ws", type=float, default=WEIGHT_STRUCTURAL,
                        help=f"Bobot struktural AST (default: {WEIGHT_STRUCTURAL})")
    parser.add_argument("--w-fp", "-wf", type=float, default=WEIGHT_SUBTREE,
                        help=f"Bobot subtree fingerprint AST (default: {WEIGHT_SUBTREE})")
    parser.add_argument("--dataset", "-d", type=str, default=DATASET_PATH,
                        help=f"Path ke test.csv (default: {DATASET_PATH})")
    parser.add_argument("--output", "-o", type=str, default=OUTPUT_PATH,
                        help=f"Path output file CSV (default: {OUTPUT_PATH})")

    args = parser.parse_args()

    evaluate_ast(
        dataset_path=args.dataset,
        threshold=args.threshold,
        weight_structural=args.w_struct,
        weight_subtree=args.w_fp,
        output_result_csv=args.output,
        kode_plag_filter=args.filter
    )
