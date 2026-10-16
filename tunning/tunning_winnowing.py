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

from winnowing_engine import StandaloneWinnowingEngine

EPS = 1e-9


def select_winnowing(rows, t_low=15.0, min_gap=3.0, margin_tol=0.3):
    cands = [r for r in rows if t_low - r["avg_non_plag_similarity"] >= min_gap]
    if not cands:
        return None, [], []
    best_margin = max(r["margin_similarity"] for r in cands)
    tied = [r for r in cands if best_margin - r["margin_similarity"] <= margin_tol + EPS]
    tied.sort(key=lambda r: (
        r["avg_similarity_only_time_ms_per_pair"],
        -r["contrast_ratio"],
        r["k"],
        -r["w"],
    ))
    return tied[0], tied, cands


def update_frozen_config(best: Dict[str, Any], frozen_config_path: str = FROZEN_CONFIG_PATH) -> None:
    """
    Memperbarui parameter Winnowing (k dan w) langsung ke frozen_config.json
    dengan tetap mempertahankan parameter konfigurasi lainnya.
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

    config["k"] = int(best["k"])
    config["w"] = int(best["w"])

    with open(frozen_config_path, "w", encoding="utf-8") as f:
        json.dump(config, f, indent=2)

    print(f"\n[OK] Hasil tuning parameter Winnowing otomatis diperbarui ke: {frozen_config_path}")
    print(f"     k : {config['k']}")
    print(f"     w : {config['w']}")



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


def load_and_preprocess_dataset(csv_path: str) -> Tuple[List[Dict[str, Any]], Dict[str, str]]:
    """
    Membaca dataset dan melakukan caching teks serta preprocessing 1x
    untuk seluruh file unik agar pencarian kombinasi parameter berjalan sangat cepat.
    """
    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"Dataset tidak ditemukan: {csv_path}")

    dataset_dir = os.path.dirname(os.path.abspath(csv_path))
    engine = StandaloneWinnowingEngine(k=5, w=4)

    raw_cache: Dict[str, str] = {}
    preprocessed_cache: Dict[str, str] = {}

    def get_preprocessed(rel_path: str) -> Tuple[str, str]:
        clean_rel = rel_path.strip().lstrip("\\/")
        full_path = os.path.join(dataset_dir, clean_rel)
        if full_path not in raw_cache:
            if not os.path.exists(full_path):
                raise FileNotFoundError(f"File kode tidak ditemukan: {full_path}")
            with open(full_path, "r", encoding="utf-8", errors="ignore") as f:
                code_text = f.read()
            raw_cache[full_path] = code_text
            preprocessed_cache[full_path] = engine.preprocess(code_text)
        return full_path, preprocessed_cache[full_path]

    pairs: List[Dict[str, Any]] = []
    with open(csv_path, mode="r", encoding="utf-8") as f:
        sample = f.readline()
        delimiter = "|" if "|" in sample else ","
        f.seek(0)

        reader = csv.DictReader(f, delimiter=delimiter)
        for row_index, row in enumerate(reader):
            kode = row.get("kode_plag", "")
            if "file1" in row and "file2" in row:
                path1, prep1 = get_preprocessed(row["file1"])
                path2, prep2 = get_preprocessed(row["file2"])
                is_plag = read_label(row)
                pairs.append({
                    "id": row.get("no", row.get("id", "")),
                    "kode_plag": kode,
                    "description": f"{kode} ({os.path.basename(row['file1'])} vs {os.path.basename(row['file2'])})",
                    "prep1": prep1,
                    "prep2": prep2,
                    "is_plag": is_plag,
                    "file1_key": path1,
                    "file2_key": path2
                })
            else:
                c1 = row["code1"]
                c2 = row["code2"]
                key1 = f"inline:{row_index}:1"
                key2 = f"inline:{row_index}:2"
                prep1 = engine.preprocess(c1)
                prep2 = engine.preprocess(c2)
                preprocessed_cache[key1] = prep1
                preprocessed_cache[key2] = prep2
                is_plag = read_label(row)
                pairs.append({
                    "id": row.get("id", row.get("no", "")),
                    "kode_plag": kode,
                    "description": row.get("description", ""),
                    "prep1": prep1,
                    "prep2": prep2,
                    "is_plag": is_plag,
                    "file1_key": key1,
                    "file2_key": key2
                })

    return pairs, preprocessed_cache


def evaluate_parameter_grid(
    dataset_path: str,
    k_list: List[int],
    w_list: List[int],
    output_summary_csv: str = None,
    frozen_config_path: str = FROZEN_CONFIG_PATH
) -> Dict[str, Any]:
    print("=" * 80)
    print("  OPTIMASI PARAMETER ENGINE WINNOWING (GRID SEARCH k & w)")
    print(f"  Dataset: {dataset_path}")
    print(f"  Kandidat k-gram ({len(k_list)} nilai): {k_list}")
    print(f"  Kandidat window w ({len(w_list)} nilai): {w_list}")
    print(f"  Total Kombinasi yang Diuji: {len(k_list) * len(w_list)}")
    print("=" * 80)

    print("\n[1/3] Membaca dataset & pra-pemrosesan kode...")
    t_start = time.perf_counter()
    pairs, preprocessed_cache = load_and_preprocess_dataset(dataset_path)
    total_pairs = len(pairs)
    n_plag = sum(1 for p in pairs if p["is_plag"] == 1)
    n_non_plag = total_pairs - n_plag
    print(f"      Total data: {total_pairs} pasangan ({n_plag} plagiat, {n_non_plag} non-plagiat)")
    print(f"      File kode unik diproses: {len(preprocessed_cache)} file (selesai dalam {(time.perf_counter() - t_start):.2f}s)")

    print("\n[2/3] Menjalankan evaluasi untuk seluruh kombinasi parameter...")
    grid_results = []
    if not pairs or not k_list or not w_list:
        raise ValueError("Dataset dan grid parameter tidak boleh kosong")
    engine = StandaloneWinnowingEngine()

    comb_idx = 0

    # Header progres
    print(f"\n{'No':<4} | {'k':<3} | {'w':<3} | {'t':<4} | {'Sim Plag (%)':<13} | {'Sim Non-Plag (%)':<17} | {'Margin (%)':<11} | {'AUC-ROC':<8} | {'Waktu (ms)':<10}")
    print("-" * 88)

    for k in k_list:
        engine.k = k

        # Cache rolling hashes untuk setiap file unik pada nilai k saat ini
        file_hashes_cache = {}
        for fkey, prep_text in preprocessed_cache.items():
            file_hashes_cache[fkey] = engine.rolling_hash(prep_text)

        for w in w_list:
            comb_idx += 1
            engine.w = w
            guarantee_t = w + k - 1

            # Hitung fingerprints untuk setiap file unik pada (k, w)
            file_fp_cache = {}
            for fkey, hashes in file_hashes_cache.items():
                file_fp_cache[fkey] = engine.select_fingerprints(hashes)

            plag_sims = []
            non_plag_sims = []
            comb_time_start = time.perf_counter()

            # Evaluasi seluruh pasangan kode
            for p in pairs:
                fp1 = file_fp_cache[p["file1_key"]]
                fp2 = file_fp_cache[p["file2_key"]]
                sim = engine.calculate_similarity_from_fingerprints(fp1, fp2)

                if p["is_plag"] == 1:
                    plag_sims.append(sim)
                else:
                    non_plag_sims.append(sim)

            elapsed_comb_ms = (time.perf_counter() - comb_time_start) * 1000.0
            avg_pair_time_ms = elapsed_comb_ms / total_pairs if total_pairs > 0 else 0.0

            avg_plag = (sum(plag_sims) / len(plag_sims)) * 100.0 if plag_sims else 0.0
            avg_non_plag = (sum(non_plag_sims) / len(non_plag_sims)) * 100.0 if non_plag_sims else 0.0
            margin = avg_plag - avg_non_plag
            contrast_ratio = avg_plag / (avg_non_plag + 1e-6)
            auc = compute_auc_roc(plag_sims, non_plag_sims)

            # Skor gabungan: Mengutamakan margin diskriminasi dan AUC-ROC
            composite_score = margin * auc

            entry = {
                "k": k,
                "w": w,
                "guarantee_threshold_t": guarantee_t,
                "avg_plag_similarity": round(avg_plag, 2),
                "avg_non_plag_similarity": round(avg_non_plag, 2),
                "margin_similarity": round(margin, 2),
                "contrast_ratio": round(contrast_ratio, 2),
                "auc_roc": auc,
                "composite_score": round(composite_score, 2),
                "avg_similarity_only_time_ms_per_pair": round(avg_pair_time_ms, 3)
            }
            grid_results.append(entry)

            print(f"{comb_idx:<4} | {k:<3} | {w:<3} | {guarantee_t:<4} | {avg_plag:>11.2f}% | {avg_non_plag:>15.2f}% | {margin:>9.2f}% | {auc:>8.4f} | {avg_pair_time_ms:>8.3f} ms")

    # Seleksi kombinasi parameter terbaik menggunakan logika multi-kriteria:
    # 1. Syarat batas aman: t_low - avg_non_plag_similarity >= min_gap (15.0 - avg_non >= 3.0)
    # 2. Margin kemiripan tertinggi (margin_tol=0.3 dianggap seri)
    # 3. Tie-break: waktu eksekusi tersingkat, contrast ratio tertinggi, k terkecil, w terbesar
    best, tied, cands = select_winnowing(grid_results, t_low=15.0, min_gap=3.0, margin_tol=0.3)
    if best is None:
        print("\n[WARNING] Tidak ada kandidat yang memenuhi syarat batas t_low - avg_non >= min_gap. Menggunakan fallback.")
        sorted_cands = sorted(grid_results, key=lambda x: (x["avg_non_plag_similarity"], -x["margin_similarity"], -x["auc_roc"]))
        best = sorted_cands[0]
        tied = [best]
        cands = sorted_cands

    print("\n" + "=" * 80)
    print("  HASIL AKHIR: PARAMETER WINNOWING TERBAIK / PALING MAKSIMAL")
    print("=" * 80)
    print(f"  >> Total kandidat lolos syarat batas (t_low - avg_non >= 3.0%) : {len(cands)} kombinasi")
    print(f"  >> Jumlah kandidat seri margin tertinggi (tol=0.3)             : {len(tied)} kombinasi")
    print(f"  >> k-gram (k)                   : {best['k']}")
    print(f"  >> Window Size (w)              : {best['w']}")
    print(f"  >> Guarantee Threshold (t)      : {best['guarantee_threshold_t']} karakter")
    print(f"  >> Rata-rata Kemiripan PLAGIAT  : {best['avg_plag_similarity']:.2f}%")
    print(f"  >> Rata-rata Kemiripan NON-PLAG : {best['avg_non_plag_similarity']:.2f}% (Safety margin ke t_low=15%: {15.0 - best['avg_non_plag_similarity']:.2f}%)")
    print(f"  >> Margin Pemisah (PLAG - NON)  : {best['margin_similarity']:.2f}%")
    print(f"  >> AUC-ROC Score                : {best['auc_roc']:.4f}")
    print(f"  >> Rasio Kontras                : {best['contrast_ratio']:.2f}x")
    print(f"  >> Waktu similarity saja / Pasangan    : {best['avg_similarity_only_time_ms_per_pair']:.3f} ms")
    print("=" * 80)

    # Tampilkan Leaderboard TOP 10 berdasarkan margin kemiripan
    leaderboard = sorted(grid_results, key=lambda x: (x["margin_similarity"], x["auc_roc"]), reverse=True)
    print("\nLEADERBOARD 10 KOMBINASI PARAMETER TERATAS:")
    print(f"{'Peringkat':<10} | {'k':<3} | {'w':<3} | {'t':<4} | {'Sim Plag (%)':<13} | {'Sim Non-Plag (%)':<17} | {'Margin (%)':<11} | {'AUC-ROC':<8}")
    print("-" * 85)
    for rank, res in enumerate(leaderboard[:10], start=1):
        print(f"#{rank:<9} | {res['k']:<3} | {res['w']:<3} | {res['guarantee_threshold_t']:<4} | {res['avg_plag_similarity']:>11.2f}% | {res['avg_non_plag_similarity']:>15.2f}% | {res['margin_similarity']:>9.2f}% | {res['auc_roc']:>8.4f}")
    print("=" * 85)

    # Breakdown per kategori plagiarisme untuk kombinasi terbaik
    print(f"\n[3/3] Evaluasi detail per kategori menggunakan parameter terbaik (k={best['k']}, w={best['w']}):")
    best_engine = StandaloneWinnowingEngine(k=best["k"], w=best["w"])
    breakdown = {}

    for p in pairs:
        k_code = p["kode_plag"]
        if k_code not in breakdown:
            breakdown[k_code] = []
        sim = best_engine.calculate_similarity_from_fingerprints(
            best_engine.get_fingerprints_from_preprocessed(p["prep1"]),
            best_engine.get_fingerprints_from_preprocessed(p["prep2"])
        )
        breakdown[k_code].append(sim * 100.0)

    print(f"{'Kategori / Kode Plag':<22} | {'Jumlah':<7} | {'Rata-rata Kemiripan (%)':<24}")
    print("-" * 60)
    for k_code, vals in sorted(breakdown.items()):
        avg_val = sum(vals) / len(vals)
        print(f"{k_code:<22} | {len(vals):<7} | {avg_val:>21.2f}%")
    print("=" * 60)

    # Simpan hasil grid search ke CSV (diurutkan berdasarkan peringkat leaderboard)
    if output_summary_csv:
        fieldnames = [
            "k", "w", "guarantee_threshold_t",
            "avg_plag_similarity", "avg_non_plag_similarity",
            "margin_similarity", "contrast_ratio", "auc_roc",
            "composite_score", "avg_similarity_only_time_ms_per_pair"
        ]
        with open(output_summary_csv, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames, extrasaction="ignore")
            writer.writeheader()
            for r in leaderboard:
                row_dict = dict(r)
                row_dict["auc_roc"] = round(row_dict["auc_roc"], 4)
                writer.writerow(row_dict)
        print(f"\n[OK] Seluruh hasil tuning parameter disimpan ke: {output_summary_csv}")

    # Update frozen_config.json secara langsung
    if frozen_config_path:
        update_frozen_config(best, frozen_config_path)

    return {
        "best_parameter": best,
        "tied_parameters": tied,
        "candidates": cands,
        "grid_results": grid_results,
        "breakdown_best": {k: sum(v)/len(v) for k, v in breakdown.items()}
    }


if __name__ == "__main__":
    default_dataset = os.path.join(DATASET_DIR, "tunning.csv")
    default_output = os.path.join(CURRENT_DIR, "tunning_res_winnowing.csv")

    parser = argparse.ArgumentParser(description="Pencarian Parameter k dan w Optimal untuk Winnowing Engine")
    parser.add_argument("--preset", "-p", choices=["quick", "standard", "full"], default="standard",
                        help="Preset kombinasi parameter: quick (16 kombinasi), standard (42 kombinasi), full (81 kombinasi)")
    parser.add_argument("--k-values", nargs="+", type=int, default=None,
                        help="Daftar nilai k kustom, contoh: --k-values 3 5 7 9")
    parser.add_argument("--w-values", nargs="+", type=int, default=None,
                        help="Daftar nilai w kustom, contoh: --w-values 3 4 5 6")
    parser.add_argument("--dataset", "-d", type=str, default=default_dataset,
                        help="Path ke file dataset CSV penalaan (default: tunning.csv)")
    parser.add_argument("--output", "-o", type=str, default=default_output,
                        help="Path file output CSV ringkasan tuning (default: tunning_res_winnowing.csv)")
    parser.add_argument("--frozen-config", "-c", type=str, default=FROZEN_CONFIG_PATH,
                        help="Path ke file frozen_config.json untuk diperbarui (default: frozen_config.json)")

    args = parser.parse_args()

    # Tentukan grid k dan w
    if args.k_values and args.w_values:
        k_list = args.k_values
        w_list = args.w_values
    elif args.preset == "quick":
        k_list = [4, 5, 6, 8]
        w_list = [3, 4, 5, 6]
    elif args.preset == "full":
        k_list = [3, 4, 5, 6, 7, 8, 10, 12, 15]
        w_list = [2, 3, 4, 5, 6, 8, 10, 12, 15]
    else:  # standard
        k_list = [3, 4, 5, 6, 7, 8, 10]
        w_list = [2, 3, 4, 5, 6, 8]

    evaluate_parameter_grid(
        dataset_path=args.dataset,
        k_list=k_list,
        w_list=w_list,
        output_summary_csv=args.output,
        frozen_config_path=args.frozen_config
    )
