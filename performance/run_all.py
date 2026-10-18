import argparse
import os
import re
import subprocess
import sys
import time
from typing import List, Dict, Any, Optional

# ==============================================================================
# RESOLUSI PATH LINGKUNGAN
# ==============================================================================
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
RESULTS_DIR = os.path.join(CURRENT_DIR, "results")
os.makedirs(RESULTS_DIR, exist_ok=True)

BENCHMARK_SCRIPTS = [
    {
        "id": "hybrid",
        "name": "Pipeline Hybrid (Proposed)",
        "script": os.path.join(CURRENT_DIR, "test_performance_hybrid.py"),
        "csv": os.path.join(RESULTS_DIR, "hybrid.csv")
    },
    {
        "id": "ast",
        "name": "Standalone Java AST",
        "script": os.path.join(CURRENT_DIR, "test_performance_ast.py"),
        "csv": os.path.join(RESULTS_DIR, "ast.csv")
    },
    {
        "id": "winnowing",
        "name": "Standalone Winnowing",
        "script": os.path.join(CURRENT_DIR, "test_performance_winn.py"),
        "csv": os.path.join(RESULTS_DIR, "winnowing.csv")
    }
]


def read_csv_stats(csv_path: str) -> Optional[Dict[str, float]]:
    """Membaca berkas CSV hasil untuk menghitung Mean, Min, Max dari kolom 'time (ms)'."""
    if not os.path.exists(csv_path):
        return None

    times: List[float] = []
    try:
        with open(csv_path, "r", encoding="utf-8") as f:
            lines = [l.strip() for l in f if l.strip()]
            for line in lines[1:]:  # Lewati header (no|time (ms)|average)
                parts = line.split("|")
                if len(parts) >= 2:
                    try:
                        times.append(float(parts[1]))
                    except ValueError:
                        pass
    except Exception as e:
        print(f"[WARNING] Gagal membaca CSV {csv_path}: {e}", file=sys.stderr)
        return None

    if not times:
        return None

    return {
        "count": len(times),
        "mean": sum(times) / len(times),
        "min": min(times),
        "max": max(times),
        "last_avg": times[-1]
    }


def main():
    # Preprocessing argumen: jika user mengetik `-10` atau `-5`, konversi ke format angka `10` atau `5`
    raw_args = sys.argv[1:]
    processed_args = []
    for arg in raw_args:
        if re.match(r"^-\d+$", arg):
            processed_args.append(arg[1:])  # Misal: "-10" -> "10"
        else:
            processed_args.append(arg)

    parser = argparse.ArgumentParser(
        description="Menjalankan pengujian performa Winnowing, Hybrid, dan AST secara bergantian sekaligus."
    )
    parser.add_argument(
        "n", type=int, nargs="?", default=None,
        help="Jumlah pengujian dicatat per metode (misal: 10 atau 11). Contoh pemanggilan: python run_all.py 10 atau python run_all.py -10"
    )
    parser.add_argument(
        "--runs", "-r", type=int, default=None,
        help="Jumlah pengujian dicatat per metode (alternatif flag)"
    )
    parser.add_argument(
        "--warmup", "-w", type=int, default=1,
        help="Jumlah iterasi warm-up yang tidak dicatat per metode (default: 1)"
    )
    parser.add_argument(
        "--no-warmup", action="store_true",
        help="Lewati iterasi warm-up untuk semua metode"
    )
    parser.add_argument(
        "--quiet", "-q", action="store_true",
        help="Mode hening: sembunyikan log detail kode dan hanya tampilkan progres waktu"
    )
    parser.add_argument(
        "--decimals", type=int, default=2,
        help="Jumlah desimal untuk waktu dan rata-rata (default: 2)"
    )

    args, unknown_args = parser.parse_known_args(processed_args)

    total_runs = args.n if args.n is not None else (args.runs if args.runs is not None else 1)
    if total_runs < 1:
        parser.error("Jumlah pengujian n harus minimal 1.")

    print("=" * 75, flush=True)
    print("   MASTER BENCHMARK RUNNER: DETEKSI PLAGIARISME JAVA", flush=True)
    print(f"   Jumlah Iterasi per Metode (N) : {total_runs} dicatat", flush=True)
    print(f"   Warm-up per Metode            : {'Dinonaktifkan' if args.no_warmup else f'{args.warmup} kali'}", flush=True)
    print(f"   Mode Tampilan                 : {'Quiet' if args.quiet else 'Verbose'}", flush=True)
    print("   Metode yang Dijalankan        : 1. Hybrid -> 2. Java AST -> 3. Winnowing", flush=True)
    print("=" * 75, flush=True)

    # Argumen yang diteruskan ke sub-skrip
    forwarded_flags: List[str] = [str(total_runs)]
    if args.no_warmup:
        forwarded_flags.append("--no-warmup")
    elif args.warmup != 1:
        forwarded_flags.extend(["--warmup", str(args.warmup)])

    if args.quiet:
        forwarded_flags.append("--quiet")

    if args.decimals != 2:
        forwarded_flags.extend(["--decimals", str(args.decimals)])

    forwarded_flags.extend(unknown_args)

    total_start_time = time.perf_counter()

    for idx, bench in enumerate(BENCHMARK_SCRIPTS, 1):
        print("\n" + "#" * 75, flush=True)
        print(f"   TAHAP {idx}/{len(BENCHMARK_SCRIPTS)}: MENJALANKAN {bench['name'].upper()}", flush=True)
        print(f"   Skrip : {os.path.basename(bench['script'])}", flush=True)
        print(f"   Output: {os.path.basename(bench['csv'])}", flush=True)
        print("#" * 75 + "\n", flush=True)

        cmd = [sys.executable, bench["script"], "--output", bench["csv"]] + forwarded_flags
        t_sub_start = time.perf_counter()
        proc = subprocess.run(cmd, cwd=CURRENT_DIR)
        t_sub_elapsed = (time.perf_counter() - t_sub_start) * 1000.0

        if proc.returncode != 0:
            print(f"\n[ERROR] Pengujian {bench['name']} gagal dengan exit code {proc.returncode}!", file=sys.stderr)
            sys.exit(proc.returncode)

        print(f"\n[SELESAI] Tahap {idx} ({bench['name']}) selesai dalam {t_sub_elapsed / 1000.0:.2f} detik.", flush=True)

    total_elapsed_sec = time.perf_counter() - total_start_time

    # ==========================================================================
    # RINGKASAN KOMPARASI HASIL
    # ==========================================================================
    print("\n\n" + "=" * 90)
    print(f"           RINGKASAN KOMPARATIF HASIL PENGUJIAN PERFORMA (N = {total_runs})")
    print("=" * 90)
    print(f"{'Metode Pengujian':<28} | {'Mean Latensi':<14} | {'Min':<12} | {'Max':<12} | {'Berkas CSV Output'}")
    print("-" * 90)

    stats_map = {}
    for bench in BENCHMARK_SCRIPTS:
        stats = read_csv_stats(bench["csv"])
        stats_map[bench["id"]] = stats
        if stats:
            mean_s = f"{stats['mean']:.{args.decimals}f} ms"
            min_s = f"{stats['min']:.{args.decimals}f} ms"
            max_s = f"{stats['max']:.{args.decimals}f} ms"
            csv_name = os.path.basename(bench["csv"])
            print(f"{bench['name']:<28} | {mean_s:<14} | {min_s:<12} | {max_s:<12} | {csv_name}")
        else:
            print(f"{bench['name']:<28} | {'[Data N/A]':<14} | {'-':<12} | {'-':<12} | {os.path.basename(bench['csv'])}")

    print("-" * 90)

    # Analisis Kecepatan (Speedup) Hybrid vs AST
    if stats_map.get("hybrid") and stats_map.get("ast"):
        mean_hyb = stats_map["hybrid"]["mean"]
        mean_ast = stats_map["ast"]["mean"]
        if mean_ast > 0:
            saved_pct = ((mean_ast - mean_hyb) / mean_ast) * 100.0
            speedup = mean_ast / mean_hyb if mean_hyb > 0 else 1.0
            print(f" Efisiensi Pipeline Hybrid vs AST : Hemat {saved_pct:.2f}% waktu komputasi ({speedup:.2f}x lebih cepat)")

    print(f" Total Waktu Keseluruhan Benchmark: {total_elapsed_sec:.2f} detik ({total_elapsed_sec / 60.0:.2f} menit)")
    print("=" * 90)


if __name__ == "__main__":
    main()
