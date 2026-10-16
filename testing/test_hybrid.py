import argparse
import csv
import json
import math
import os
import sys
import time
from typing import List, Dict, Any, Tuple

# Direktori dasar
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
PUBLIKASI_DIR = os.path.dirname(CURRENT_DIR)
ENGINE_DIR = os.path.join(PUBLIKASI_DIR, "engine")
DATASET_DIR = os.path.join(PUBLIKASI_DIR, "dataset")

if ENGINE_DIR not in sys.path:
    sys.path.insert(0, ENGINE_DIR)

from dataset_utils import read_label

from hybrid_engine import StandaloneHybridEngine
from winnowing_engine import StandaloneWinnowingEngine
from java_ast_engine import StandaloneJavaASTEngine

# ==============================================================================
# PENGATURAN PARAMETER PIPELINE HYBRID DARI FROZEN CONFIG
# ==============================================================================
FROZEN_CONFIG_PATH = os.path.join(PUBLIKASI_DIR, "frozen_config.json")
CONFIG = {}
if os.path.exists(FROZEN_CONFIG_PATH):
    with open(FROZEN_CONFIG_PATH, "r", encoding="utf-8") as f:
        CONFIG = json.load(f)

WINNOWING_THRESHOLD = float(CONFIG.get("winnowing_threshold", StandaloneWinnowingEngine.DEFAULT_THRESHOLD))
K = int(CONFIG.get("k", StandaloneHybridEngine.DEFAULT_K))
W = int(CONFIG.get("w", StandaloneHybridEngine.DEFAULT_W))
LOWER_THRESHOLD = float(CONFIG.get("lower_threshold", StandaloneHybridEngine.DEFAULT_LOWER_THRESHOLD))
UPPER_THRESHOLD = float(CONFIG.get("upper_threshold", StandaloneHybridEngine.DEFAULT_UPPER_THRESHOLD))
AST_THRESHOLD = float(CONFIG.get("ast_threshold", StandaloneHybridEngine.DEFAULT_AST_THRESHOLD))

# Bobot Komponen Verifikasi AST (Tahap 2)
WEIGHT_STRUCTURAL = float(CONFIG.get("weight_structural", StandaloneHybridEngine.DEFAULT_WEIGHT_STRUCTURAL))
WEIGHT_SUBTREE = float(CONFIG.get("weight_subtree", StandaloneHybridEngine.DEFAULT_WEIGHT_SUBTREE))

# Konfigurasi Path Berkas Dataset dan Output
DATASET_PATH = os.path.join(DATASET_DIR, "test.csv")
OUTPUT_PATH = os.path.join(CURRENT_DIR, "test_hybrid.csv")
# ==============================================================================


def load_dataset(csv_path: str = DATASET_PATH) -> List[Dict[str, Any]]:
    """
    Membaca dataset berpasangan dari CSV dan memuat isi file Java.
    """
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
            if "file1" in row and "file2" in row:
                c1 = read_code_file(row["file1"])
                c2 = read_code_file(row["file2"])
                dataset.append({
                    "id": row.get("no", row.get("id", "")),
                    "kode_plag": row.get("kode_plag", ""),
                    "description": f"{row.get('kode_plag', '')} ({os.path.basename(row['file1'])} vs {os.path.basename(row['file2'])})",
                    "file1": row["file1"],
                    "file2": row["file2"],
                    "code1": c1,
                    "code2": c2,
                    "label": read_label(row)
                })
            else:
                dataset.append({
                    "id": row.get("id", row.get("no", "")),
                    "kode_plag": row.get("kode_plag", ""),
                    "description": row.get("description", ""),
                    "code1": row["code1"],
                    "code2": row["code2"],
                    "label": read_label(row)
                })
    return dataset


def get_category_desc(kode_plag: str) -> str:
    """Mengembalikan deskripsi kategori plagiarisme yang informatif."""
    mapping = {
        "PLAG_L1_01": "L1.01: Whitespace, Indentasi & Komentar",
        "PLAG_L1_02": "L1.02: Identifier Renaming (Variabel & Fungsi)",
        "PLAG_L1_03": "L1.03: Modifikasi Konstanta & Literal",
        "PLAG_L1_04": "L1.04: Format String & Literal Teks",
        "PLAG_L1_05": "L1.05: Pengubahan Tipe Data Ekivalen",
        "PLAG_L1_06": "L1.06: Pengubahan Pesan Output Konsol",
        "PLAG_L1_07": "L1.07: Kombinasi Campuran Modifikasi Leksikal",
        "PLAG_L2_01": "L2.01: Restrukturisasi Percabangan (If/Switch)",
        "PLAG_L2_02": "L2.02: Penggantian Perulangan (For/While/Do-While)",
        "PLAG_L2_03": "L2.03: Permutasi Deklarasi Method",
        "PLAG_L2_05": "L2.05: Injeksi Dead-Code & Redundant Variable",
        "PLAG_L3_01": "L3.01: Refaktorisasi Method Extraction",
        "PLAG_L3_03": "L3.03: Refaktorisasi Inline Method",
        "NON_PLAG": "NON_PLAG: Pasangan Kode Berbeda (Non-Plagiat)"
    }
    return mapping.get(kode_plag, kode_plag)


def evaluate_hybrid_effectiveness(
    dataset_path: str = DATASET_PATH,
    k: int = K,
    w: int = W,
    lower_threshold: float = LOWER_THRESHOLD,
    upper_threshold: float = UPPER_THRESHOLD,
    ast_threshold: float = AST_THRESHOLD,
    weight_structural: float = WEIGHT_STRUCTURAL,
    weight_subtree: float = WEIGHT_SUBTREE,
    output_detail_csv: str = OUTPUT_PATH,
    winnowing_threshold: float = WINNOWING_THRESHOLD
) -> Dict[str, Any]:
    print("=" * 95)
    print("   PENGUJIAN EFEKTIVITAS PIPELINE HYBRID (WINNOWING + AST ADAPTIF 3-ZONA)")
    print(f"   Parameter Winnowing: k={k}, w={w} (Hasil Tuning Optimal Grid Search Rank #1)")
    print(f"   Ambang Batas Winnowing: Bawah <= {lower_threshold*100:.0f}% (Fast-Exit), Atas >= {upper_threshold*100:.0f}% (Fast-Exit)")
    print(f"   Ambang Batas Verifikasi AST (Zona Abu-abu): {ast_threshold*100:.0f}%")
    print(f"   Bobot Verifikasi AST: {weight_structural*100:.0f}% Structural + {weight_subtree*100:.0f}% Subtree Fingerprint")
    print(f"   Threshold baseline Winnowing: {winnowing_threshold:.2f}")
    print(f"   Dataset: {dataset_path}")
    print("=" * 95)

    dataset = load_dataset(dataset_path)
    total_samples = len(dataset)
    if not total_samples:
        raise ValueError("Dataset pengujian kosong")

    winnowing_engine = StandaloneWinnowingEngine(k=k, w=w)
    ast_engine = StandaloneJavaASTEngine(weight_structural=weight_structural, weight_subtree=weight_subtree)
    hybrid_engine = StandaloneHybridEngine(
        k=k, w=w,
        winnowing_lower_threshold=lower_threshold,
        winnowing_upper_threshold=upper_threshold,
        ast_threshold=ast_threshold,
        weight_structural=weight_structural,
        weight_subtree=weight_subtree
    )

    details = []

    # Akumulator Waktu & Kemiripan
    total_time_winn = 0.0
    total_time_ast = 0.0
    total_time_hybrid = 0.0

    total_sim_winn = 0.0
    total_sim_ast = 0.0
    total_sim_hybrid = 0.0

    # Metrik Klasifikasi
    cm_winn = {"TP": 0, "FP": 0, "TN": 0, "FN": 0}
    cm_ast = {"TP": 0, "FP": 0, "TN": 0, "FN": 0}
    cm_hybrid = {"TP": 0, "FP": 0, "TN": 0, "FN": 0}

    # Breakdown per Kategori
    categories = {}
    z_low_key = f"Low Fast-Exit (<= {lower_threshold*100:.0f}%)"
    z_grey_key = f"Grey Zone ({lower_threshold*100:.0f}% - {upper_threshold*100:.0f}%)"
    z_high_key = f"High Fast-Exit (>= {upper_threshold*100:.0f}%)"
    zone_stats = {
        z_low_key: {"count": 0, "time_hybrid": 0.0, "time_ast": 0.0, "time_winn": 0.0},
        z_grey_key: {"count": 0, "time_hybrid": 0.0, "time_ast": 0.0, "time_winn": 0.0},
        z_high_key: {"count": 0, "time_hybrid": 0.0, "time_ast": 0.0, "time_winn": 0.0},
    }

    print(f"\n{'ID':<5} | {'Kategori':<12} | {'Label':<5} | {'Winn%':<7} | {'AST%':<7} | {'Hyb%':<7} | {'AST(ms)':<8} | {'Hyb(ms)':<8} | {'Hemat(ms)':<9} | {'Hemat%':<7} | {'Zona Keputusan':<22}")
    print("-" * 115)

    for item in dataset:
        c1, c2 = item["code1"], item["code2"]
        actual = item["label"]
        sid = item["id"]
        k_code = item["kode_plag"]

        # 1. Eksekusi Winnowing Saja
        t0 = time.perf_counter()
        w_sim = winnowing_engine.calculate_similarity(c1, c2)
        w_time = (time.perf_counter() - t0) * 1000.0

        # 2. Eksekusi AST Saja
        t0 = time.perf_counter()
        a_res = ast_engine.detect(c1, c2, threshold=ast_threshold)
        a_time = (time.perf_counter() - t0) * 1000.0
        # Hitung skor AST dengan bobot yang dikonfigurasi
        struct_sim = a_res["structural_similarity"]
        fp_sim = a_res["fingerprint_similarity"]
        a_sim = a_res["similarity"]

        # 3. Eksekusi Pipeline Hybrid
        t0 = time.perf_counter()
        h_res = hybrid_engine.detect(
            c1, c2,
            winnowing_lower_threshold=lower_threshold,
            winnowing_upper_threshold=upper_threshold,
            ast_threshold=ast_threshold,
            weight_structural=weight_structural,
            weight_subtree=weight_subtree
        )
        h_time = (time.perf_counter() - t0) * 1000.0
        h_sim = h_res["similarity"]
        ast_exec = h_res["ast_executed"]
        decision_zone = h_res["decision_zone"]

        # Selisih Waktu (AST vs Hybrid)
        time_diff_ast_vs_hyb = a_time - h_time
        pct_time_saved = ((a_time - h_time) / a_time * 100.0) if a_time > 0 else 0.0
        speedup = (a_time / h_time) if h_time > 0 else 1.0

        # Selisih Kemiripan (Hybrid vs Winnowing)
        sim_diff_hyb_vs_winn = (h_sim - w_sim) * 100.0

        # Akumulasi Waktu & Sim
        total_time_winn += w_time
        total_time_ast += a_time
        total_time_hybrid += h_time

        total_sim_winn += w_sim
        total_sim_ast += a_sim
        total_sim_hybrid += h_sim

        # Klasifikasi menggunakan threshold masing-masing engine
        pred_w = int(w_sim >= winnowing_threshold)
        pred_a = int(a_res["is_plagiarized"])
        pred_h = int(h_res["is_plagiarized"])

        # Evaluasi Kasus Penyelamatan (False Negative Winnowing diselamatkan Hybrid)
        fn_rescued = (actual == 1 and pred_w == 0 and pred_h == 1)

        # Update Confusion Matrix Winnowing
        if actual == 1 and pred_w == 1:
            cm_winn["TP"] += 1; status_w = "TP"
        elif actual == 0 and pred_w == 1:
            cm_winn["FP"] += 1; status_w = "FP"
        elif actual == 0 and pred_w == 0:
            cm_winn["TN"] += 1; status_w = "TN"
        else:
            cm_winn["FN"] += 1; status_w = "FN"

        # Update Confusion Matrix AST
        if actual == 1 and pred_a == 1:
            cm_ast["TP"] += 1; status_a = "TP"
        elif actual == 0 and pred_a == 1:
            cm_ast["FP"] += 1; status_a = "FP"
        elif actual == 0 and pred_a == 0:
            cm_ast["TN"] += 1; status_a = "TN"
        else:
            cm_ast["FN"] += 1; status_a = "FN"

        # Update Confusion Matrix Hybrid
        if actual == 1 and pred_h == 1:
            cm_hybrid["TP"] += 1; status_h = "TP"
        elif actual == 0 and pred_h == 1:
            cm_hybrid["FP"] += 1; status_h = "FP"
        elif actual == 0 and pred_h == 0:
            cm_hybrid["TN"] += 1; status_h = "TN"
        else:
            cm_hybrid["FN"] += 1; status_h = "FN"

        # Update Statistik Zona Keputusan
        if "Low" in decision_zone:
            z_key = z_low_key
        elif "High" in decision_zone:
            z_key = z_high_key
        else:
            z_key = z_grey_key

        zone_stats[z_key]["count"] += 1
        zone_stats[z_key]["time_hybrid"] += h_time
        zone_stats[z_key]["time_ast"] += a_time
        zone_stats[z_key]["time_winn"] += w_time

        # Breakdown per Kategori
        if k_code not in categories:
            categories[k_code] = {
                "count": 0,
                "label": actual,
                "sims_winn": [], "sims_ast": [], "sims_hyb": [],
                "times_winn": [], "times_ast": [], "times_hyb": [],
                "ast_calls": 0, "rescued_count": 0
            }
        cat = categories[k_code]
        cat["count"] += 1
        cat["sims_winn"].append(w_sim)
        cat["sims_ast"].append(a_sim)
        cat["sims_hyb"].append(h_sim)
        cat["times_winn"].append(w_time)
        cat["times_ast"].append(a_time)
        cat["times_hyb"].append(h_time)
        if ast_exec:
            cat["ast_calls"] += 1
        if fn_rescued:
            cat["rescued_count"] += 1

        zone_short = "Low Fast-Exit" if "Low" in decision_zone else ("High Fast-Exit" if "High" in decision_zone else "Grey Zone (AST)")
        rescue_mark = " [*RESCUED]" if fn_rescued else ""
        print(f"{sid:<5} | {k_code:<12} | {actual:<5} | {w_sim*100:>5.1f}% | {a_sim*100:>5.1f}% | {h_sim*100:>5.1f}% | {a_time:>6.2f} ms | {h_time:>6.2f} ms | {time_diff_ast_vs_hyb:>7.2f} ms | {pct_time_saved:>5.1f}% | {zone_short:<16}{rescue_mark}")

        details.append({
            "id": sid,
            "kode_plag": k_code,
            "deskripsi": item["description"],
            "actual_label": actual,
            "winnowing_sim_pct": round(w_sim * 100, 2),
            "winnowing_time_ms": round(w_time, 3),
            "winnowing_pred": pred_w,
            "winnowing_eval_status": status_w,
            "ast_sim_pct": round(a_sim * 100, 2),
            "ast_time_ms": round(a_time, 3),
            "ast_pred": pred_a,
            "ast_eval_status": status_a,
            "hybrid_sim_pct": round(h_sim * 100, 2),
            "hybrid_time_ms": round(h_time, 3),
            "hybrid_pred": pred_h,
            "hybrid_eval_status": status_h,
            "hybrid_decision_zone": decision_zone,
            "hybrid_ast_executed": ast_exec,
            "selisih_waktu_ast_vs_hybrid_ms": round(time_diff_ast_vs_hyb, 3),
            "persen_penghematan_waktu_pct": round(pct_time_saved, 2),
            "rasio_percepatan_speedup": round(speedup, 2),
            "selisih_kemiripan_hybrid_vs_winn_pct": round(sim_diff_hyb_vs_winn, 2),
            "fn_rescued_by_hybrid": fn_rescued
        })

    # Perhitungan Statistik Rata-rata & Global
    avg_t_winn = total_time_winn / total_samples
    avg_t_ast = total_time_ast / total_samples
    avg_t_hybrid = total_time_hybrid / total_samples

    total_time_saved_ms = total_time_ast - total_time_hybrid
    total_time_saved_sec = total_time_saved_ms / 1000.0
    overall_time_savings_pct = (total_time_saved_ms / total_time_ast * 100.0) if total_time_ast > 0 else 0.0
    global_speedup = (total_time_ast / total_time_hybrid) if total_time_hybrid > 0 else 1.0

    # Metrik Klasifikasi
    def calc_metrics(cm):
        tp, fp, tn, fn = cm["TP"], cm["FP"], cm["TN"], cm["FN"]
        acc = (tp + tn) / (tp + fp + tn + fn) * 100.0 if (tp + fp + tn + fn) > 0 else 0.0
        prec = tp / (tp + fp) * 100.0 if (tp + fp) > 0 else 0.0
        rec = tp / (tp + fn) * 100.0 if (tp + fn) > 0 else 0.0
        f1 = (2 * prec * rec) / (prec + rec) if (prec + rec) > 0 else 0.0
        return {"Accuracy": acc, "Precision": prec, "Recall": rec, "F1": f1, "TP": tp, "FP": fp, "TN": tn, "FN": fn}

    metrics_winn = calc_metrics(cm_winn)
    metrics_ast = calc_metrics(cm_ast)
    metrics_hybrid = calc_metrics(cm_hybrid)

    # Cetak Ringkasan
    print("\n" + "=" * 95)
    print("   RINGKASAN HASIL EVALUASI KOMPARASI TIGA ENGINE:")
    print("=" * 95)
    print(f"{'Metrik':<25} | {'Winnowing Saja':<18} | {'AST Saja':<18} | {'Hybrid Pipeline':<18}")
    print("-" * 85)
    print(f"{'Akurasi (%)':<25} | {metrics_winn['Accuracy']:>15.2f}% | {metrics_ast['Accuracy']:>15.2f}% | {metrics_hybrid['Accuracy']:>15.2f}%")
    print(f"{'Presisi (%)':<25} | {metrics_winn['Precision']:>15.2f}% | {metrics_ast['Precision']:>15.2f}% | {metrics_hybrid['Precision']:>15.2f}%")
    print(f"{'Recall (%)':<25} | {metrics_winn['Recall']:>15.2f}% | {metrics_ast['Recall']:>15.2f}% | {metrics_hybrid['Recall']:>15.2f}%")
    print(f"{'F1-Score (%)':<25} | {metrics_winn['F1']:>15.2f}% | {metrics_ast['F1']:>15.2f}% | {metrics_hybrid['F1']:>15.2f}%")
    print(f"{'Rata-rata Waktu (ms)':<25} | {avg_t_winn:>15.3f}   | {avg_t_ast:>15.3f}   | {avg_t_hybrid:>15.3f}  ")
    print(f"{'Efisiensi vs AST':<25} | {'-':>18} | {'Basis (1.00x)':>18} | {f'{overall_time_savings_pct:.1f}% ({global_speedup:.2f}x)':>18}")
    print("=" * 85)

    # Simpan CSV Detail
    if output_detail_csv:
        fieldnames = [
            "id", "kode_plag", "deskripsi", "actual_label",
            "winnowing_sim_pct", "winnowing_time_ms", "winnowing_pred", "winnowing_eval_status",
            "ast_sim_pct", "ast_time_ms", "ast_pred", "ast_eval_status",
            "hybrid_sim_pct", "hybrid_time_ms", "hybrid_pred", "hybrid_eval_status",
            "hybrid_decision_zone", "hybrid_ast_executed",
            "selisih_waktu_ast_vs_hybrid_ms", "persen_penghematan_waktu_pct", "rasio_percepatan_speedup",
            "selisih_kemiripan_hybrid_vs_winn_pct", "fn_rescued_by_hybrid"
        ]
        os.makedirs(os.path.dirname(os.path.abspath(output_detail_csv)), exist_ok=True)
        with open(output_detail_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(details)
        print(f"\n[OK] CSV Hasil Pengujian Hybrid disimpan ke: {output_detail_csv}")

    return {
        "metrics_winn": metrics_winn,
        "metrics_ast": metrics_ast,
        "metrics_hybrid": metrics_hybrid,
        "details": details
    }


if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="Pengujian Komparasi Efektivitas Pipeline Hybrid (Winnowing vs AST vs Hybrid)"
    )
    parser.add_argument("--k", type=int, default=K,
                        help=f"Ukuran k-gram Winnowing (default: {K})")
    parser.add_argument("--w", type=int, default=W,
                        help=f"Ukuran window Winnowing (default: {W})")
    parser.add_argument("--lower", "-l", type=float, default=LOWER_THRESHOLD,
                        help=f"Ambang batas bawah Winnowing (default: {LOWER_THRESHOLD})")
    parser.add_argument("--upper", "-u", type=float, default=UPPER_THRESHOLD,
                        help=f"Ambang batas atas Winnowing (default: {UPPER_THRESHOLD})")
    parser.add_argument("--winnowing-threshold", type=float, default=WINNOWING_THRESHOLD,
                        help=f"Threshold baseline Winnowing (default: {WINNOWING_THRESHOLD})")
    parser.add_argument("--ast-threshold", "-a", type=float, default=AST_THRESHOLD,
                        help=f"Ambang batas verifikasi AST (default: {AST_THRESHOLD})")
    parser.add_argument("--w-struct", "-ws", type=float, default=WEIGHT_STRUCTURAL,
                        help=f"Bobot struktural AST (default: {WEIGHT_STRUCTURAL})")
    parser.add_argument("--w-fp", "-wf", type=float, default=WEIGHT_SUBTREE,
                        help=f"Bobot subtree fingerprint AST (default: {WEIGHT_SUBTREE})")
    parser.add_argument("--dataset", "-d", type=str, default=DATASET_PATH,
                        help=f"Path file dataset CSV (default: {DATASET_PATH})")
    parser.add_argument("--output", "-o", type=str, default=OUTPUT_PATH,
                        help=f"Path output CSV detail per pasangan (default: {OUTPUT_PATH})")

    args = parser.parse_args()

    evaluate_hybrid_effectiveness(
        dataset_path=args.dataset,
        k=args.k,
        w=args.w,
        lower_threshold=args.lower,
        upper_threshold=args.upper,
        ast_threshold=args.ast_threshold,
        weight_structural=args.w_struct,
        weight_subtree=args.w_fp,
        winnowing_threshold=args.winnowing_threshold,
        output_detail_csv=args.output
    )
