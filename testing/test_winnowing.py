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

from winnowing_engine import StandaloneWinnowingEngine

# ==============================================================================
# PENGATURAN PARAMETER DARI FROZEN CONFIG
# ==============================================================================
FROZEN_CONFIG_PATH = os.path.join(PUBLIKASI_DIR, "frozen_config.json")
CONFIG = {}
if os.path.exists(FROZEN_CONFIG_PATH):
    with open(FROZEN_CONFIG_PATH, "r", encoding="utf-8") as f:
        CONFIG = json.load(f)

THRESHOLD = float(CONFIG.get("winnowing_threshold", StandaloneWinnowingEngine.DEFAULT_THRESHOLD))
K = int(CONFIG.get("k", StandaloneWinnowingEngine.DEFAULT_K))
W = int(CONFIG.get("w", StandaloneWinnowingEngine.DEFAULT_W))
FILTER_KODE_PLAG = "ALL"            # Filter pengujian: "ALL" untuk seluruh sampel, atau pola (misal: "PLAG_L1*")

# Konfigurasi Path Berkas Dataset dan Output
DATASET_PATH = os.path.join(DATASET_DIR, "test.csv")
OUTPUT_PATH = os.path.join(CURRENT_DIR, "test_winn_rest.csv")
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
                    "file1": row.get("file1", ""),
                    "file2": row.get("file2", ""),
                    "code1": row["code1"],
                    "code2": row["code2"],
                    "label": read_label(row)
                })
    return dataset


def evaluate_winnowing(
    dataset_path: str = DATASET_PATH,
    k: int = K,
    w: int = W,
    output_result_csv: str = OUTPUT_PATH,
    kode_plag_filter: str = FILTER_KODE_PLAG,
    threshold: float = THRESHOLD
) -> Dict[str, Any]:
    print("=" * 75)
    print("  UJICOBA DETEKSI PLAGIARISME JAVA: ENGINE WINNOWING")
    print(f"  Dataset: {dataset_path}")
    print(f"  Parameter: k-gram = {k}, window size (w) = {w}, threshold = {threshold:.2f}")
    if kode_plag_filter and kode_plag_filter.upper() != "ALL":
        print(f"  Filter Kode Plag: {kode_plag_filter}")
    print("=" * 75)

    dataset = load_dataset(dataset_path, kode_plag_filter=kode_plag_filter)
    engine = StandaloneWinnowingEngine(k=k, w=w)

    results = []
    total_time = 0.0

    print(f"{'ID':<8} | {'Kode Plag':<15} | {'Kemiripan (%)':<16} | {'Waktu (ms)':<12}")
    print("-" * 60)

    for item in dataset:
        start_t = time.perf_counter()
        det_result = engine.detect(item["code1"], item["code2"], threshold=threshold)
        elapsed_ms = (time.perf_counter() - start_t) * 1000.0
        total_time += elapsed_ms

        sim = det_result["similarity"]
        sim_pct = sim * 100.0

        results.append({
            "id": item["id"],
            "kode_plag": item.get("kode_plag", ""),
            "description": item["description"],
            "actual_label": item.get("label", 0),
            "predicted_label": int(det_result["is_plagiarized"]),
            "threshold": threshold,
            "similarity": round(sim, 4),
            "similarity_percentage": f"{sim_pct:.2f}%",
            "execution_time_ms": round(elapsed_ms, 3),
            "shared_fp": det_result["shared_fingerprints"],
            "fp_count_1": det_result["fingerprints_count_1"],
            "fp_count_2": det_result["fingerprints_count_2"]
        })

        print(f"{item['id']:<8} | {item.get('kode_plag', ''):<15} | {sim_pct:>13.2f}% | {elapsed_ms:>9.2f} ms")

    metrics = classification_metrics(results)
    print(f"Metrik klasifikasi (%): {metrics}")
    total_samples = len(dataset)
    sim_values = [r["similarity"] * 100.0 for r in results]
    avg_sim = (sum(sim_values) / total_samples) if total_samples > 0 else 0.0
    min_sim = min(sim_values) if total_samples > 0 else 0.0
    max_sim = max(sim_values) if total_samples > 0 else 0.0
    avg_time = (total_time / total_samples) if total_samples > 0 else 0.0

    print("-" * 60)
    print("RINGKASAN HASIL EVALUASI PERSENTASE KEMIRIPAN:")
    print(f"Total Pasangan Kode   : {total_samples}")
    print(f"Rata-rata Kemiripan   : {avg_sim:.2f}%")
    print(f"Kemiripan Terendah    : {min_sim:.2f}%")
    print(f"Kemiripan Tertinggi   : {max_sim:.2f}%")
    print(f"Rata-rata Waktu       : {avg_time:.3f} ms / pasangan kode")
    print("=" * 60)

    # Breakdown per kode_plag
    breakdown = {}
    for r in results:
        k_code = r["kode_plag"]
        if k_code not in breakdown:
            breakdown[k_code] = {"total": 0, "sims": [], "times": []}
        breakdown[k_code]["total"] += 1
        breakdown[k_code]["sims"].append(r["similarity"] * 100.0)
        breakdown[k_code]["times"].append(r["execution_time_ms"])

    if len(breakdown) > 1:
        print("\nBREAKDOWN PERSENTASE KEMIRIPAN PER KATEGORI PLAGIARISME:")
        print(f"{'Kode Plag':<15} | {'Total':<6} | {'Rata-rata Sim (%)':<20} | {'Rata-rata Waktu (ms)':<22}")
        print("-" * 72)
        for k_code, stats in sorted(breakdown.items()):
            tot = stats["total"]
            avg_sim_k = (sum(stats["sims"]) / tot) if tot > 0 else 0.0
            avg_t_k = sum(stats["times"]) / tot if tot > 0 else 0.0
            print(f"{k_code:<15} | {tot:<6} | {avg_sim_k:>17.2f}% | {avg_t_k:>19.2f} ms")
        print("=" * 72)

    # Simpan hasil detail ke CSV
    if output_result_csv:
        fieldnames = [
            "id", "kode_plag", "description", "actual_label", "predicted_label", "threshold", "similarity", "similarity_percentage",
            "execution_time_ms", "shared_fp", "fp_count_1", "fp_count_2"
        ]
        with open(output_result_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            for r in results:
                writer.writerow(r)
        print(f"\n[OK] Hasil detail kemiripan disimpan ke: {output_result_csv}")

    return {
        "engine": "Winnowing",
        "total_samples": total_samples,
        "avg_similarity_percent": avg_sim,
        "min_similarity_percent": min_sim,
        "max_similarity_percent": max_sim,
        "avg_time_ms": avg_time,
        "breakdown": breakdown,
        "metrics": metrics,
        "details": results
    }


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Ujicoba Deteksi Plagiarisme Java: Engine Winnowing (Persentase Kemiripan)")
    parser.add_argument("--filter", "-f", type=str, default=FILTER_KODE_PLAG,
                        help=f"Filter kode_plag pattern (default: {FILTER_KODE_PLAG})")
    parser.add_argument("--threshold", "-t", type=float, default=THRESHOLD,
                        help=f"Threshold deteksi plagiarisme (default: {THRESHOLD})")
    parser.add_argument("--k", "-k", type=int, default=K,
                        help=f"Ukuran k-gram (default: {K})")
    parser.add_argument("--w", "-w", type=int, default=W,
                        help=f"Ukuran window w (default: {W})")
    parser.add_argument("--dataset", "-d", type=str, default=DATASET_PATH,
                        help=f"Path ke file dataset CSV uji (default: {DATASET_PATH})")
    parser.add_argument("--output", "-o", type=str, default=OUTPUT_PATH,
                        help=f"Path output file CSV (default: {OUTPUT_PATH})")

    args = parser.parse_args()

    evaluate_winnowing(
        dataset_path=args.dataset,
        threshold=args.threshold,
        k=args.k,
        w=args.w,
        output_result_csv=args.output,
        kode_plag_filter=args.filter
    )
