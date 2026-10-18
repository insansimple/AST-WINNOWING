import argparse
import os
import subprocess
import sys
import time
from typing import List, Dict, Any

# ==============================================================================
# RESOLUSI PATH LINGKUNGAN DAN DEFAULT OUTPUT
# ==============================================================================
CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
PUBLIKASI_DIR = os.path.dirname(CURRENT_DIR)
TESTING_DIR = os.path.join(PUBLIKASI_DIR, "testing")
TEST_AST_PATH = os.path.join(TESTING_DIR, "test_ast.py")
RESULTS_DIR = os.path.join(CURRENT_DIR, "results")
DEFAULT_OUTPUT_CSV = os.path.join(RESULTS_DIR, "test_performance_ast_result.csv")


def run_single_test_ast(
    script_path: str = TEST_AST_PATH,
    extra_args: List[str] = None,
    quiet: bool = False
) -> float:
    """
    Menjalankan script target menggunakan subprocess dan menghitung waktu eksekusi
    dengan time.perf_counter(). Mengembalikan durasi waktu dalam satuan ms.
    """
    if not os.path.exists(script_path):
        raise FileNotFoundError(f"Script target tidak ditemukan: {script_path}")

    extra_args = extra_args or []
    cmd = [sys.executable, script_path] + extra_args

    # Pewaktu presisi tinggi
    start_time = time.perf_counter()
    proc = subprocess.run(
        cmd,
        cwd=os.path.dirname(script_path),
        stdout=subprocess.DEVNULL if quiet else None,
        stderr=subprocess.DEVNULL if quiet else None
    )
    end_time = time.perf_counter()

    if proc.returncode != 0:
        print(
            f"\n[ERROR] Eksekusi {os.path.basename(script_path)} gagal dengan exit code {proc.returncode}!",
            file=sys.stderr
        )
        sys.exit(proc.returncode)

    elapsed_ms = (end_time - start_time) * 1000.0
    return elapsed_ms


def format_num(val: float, decimals: int) -> str:
    """Format angka sesuai jumlah desimal."""
    if decimals <= 0:
        return f"{int(round(val))}"
    return f"{val:.{decimals}f}"


def main():
    parser = argparse.ArgumentParser(
        description="Mengukur performa runtime test_ast.py dan menyimpan hasilnya ke test_performance_ast_result.csv"
    )
    parser.add_argument(
        "n", type=int, nargs="?", default=None,
        help="Jumlah kali pengujian dicatat berurutan (misal: 11). Default: 1"
    )
    parser.add_argument(
        "--runs", "-r", type=int, default=None,
        help="Jumlah kali pengujian dicatat berurutan (alternatif flag)"
    )
    parser.add_argument(
        "--warmup", "-w", type=int, default=1,
        help="Jumlah iterasi warm-up yang dijalankan sebelum pengujian dicatat (default: 1)"
    )
    parser.add_argument(
        "--no-warmup", action="store_true",
        help="Lewati iterasi warm-up (langsung mulai pengujian yang dicatat)"
    )
    parser.add_argument(
        "--output", "-o", type=str, default=DEFAULT_OUTPUT_CSV,
        help=f"Path berkas output CSV (default: {DEFAULT_OUTPUT_CSV})"
    )
    parser.add_argument(
        "--quiet", "-q", action="store_true",
        help="Sembunyikan output langsung dari script test_ast.py"
    )
    parser.add_argument(
        "--append", "-a", action="store_true",
        help="Tambahkan hasil ke berkas CSV yang sudah ada (melanjutkan nomor urut)"
    )
    parser.add_argument(
        "--decimals", type=int, default=2,
        help="Jumlah angka di belakang koma untuk time (ms) dan average (default: 2, gunakan 0 untuk integer)"
    )
    parser.add_argument(
        "--target", type=str, default=TEST_AST_PATH,
        help=f"Path ke script test_ast.py (default: {TEST_AST_PATH})"
    )
    parser.add_argument(
        "--no-performance", action="store_true",
        help="Jalankan target script tanpa flag -performance (tetap simpan CSV & append hasil)"
    )

    args, unknown_args = parser.parse_known_args()

    total_runs = args.n if args.n is not None else (args.runs if args.runs is not None else 1)
    if total_runs < 1:
        parser.error("Jumlah pengujian n harus minimal 1.")

    num_warmup = 0 if args.no_warmup else max(0, args.warmup)

    # Pastikan flag -performance diteruskan secara default ke test_ast.py agar berjalan maksimal
    extra_flags = list(unknown_args)
    if not args.no_performance and "-performance" not in extra_flags and "--performance" not in extra_flags and "-p" not in extra_flags:
        extra_flags.append("-performance")

    output_csv = os.path.abspath(args.output)
    os.makedirs(os.path.dirname(output_csv), exist_ok=True)

    # Deteksi mode append atau overwrite
    existing_times: List[float] = []
    start_no = 1

    if args.append and os.path.exists(output_csv):
        try:
            with open(output_csv, "r", encoding="utf-8") as f:
                lines = [line.strip() for line in f if line.strip()]
                # Lewati header (no|time (ms)|average)
                for line in lines[1:]:
                    parts = line.split("|")
                    if len(parts) >= 2:
                        try:
                            existing_times.append(float(parts[1]))
                        except ValueError:
                            pass
            start_no = len(existing_times) + 1
        except Exception as e:
            print(f"[WARNING] Gagal membaca CSV lama untuk append: {e}", file=sys.stderr)

    file_mode = "a" if (args.append and os.path.exists(output_csv) and os.path.getsize(output_csv) > 0) else "w"

    print("=" * 70, flush=True)
    print("  PENGUKURAN KINERJA WAKTU EKSEKUSI JAVA AST", flush=True)
    print(f"  Target Script : {args.target}", flush=True)
    print(f"  Output CSV    : {output_csv}", flush=True)
    print(f"  Jumlah Uji (n): {total_runs} dicatat (+ {num_warmup} warm-up)", flush=True)
    print(f"  Total Eksekusi: {total_runs + num_warmup} kali", flush=True)
    print(f"  Pewaktu       : time.perf_counter()", flush=True)
    print(f"  Format Kolom  : no|time (ms)|average", flush=True)
    if extra_flags:
        print(f"  Argumen Pas   : {' '.join(extra_flags)}", flush=True)
    print("=" * 70, flush=True)

    # ==========================================================================
    # 1. TAHAP WARM-UP (TIDAK DICATAT KE CSV)
    # ==========================================================================
    if num_warmup > 0:
        for w_idx in range(num_warmup):
            print(f"\n>>> [WARM-UP {w_idx + 1}/{num_warmup}] Menjalankan warm-up (tidak dicatat ke CSV) ... <<<", flush=True)
            warmup_ms = run_single_test_ast(
                script_path=args.target,
                extra_args=extra_flags,
                quiet=args.quiet
            )
            print(f">>> [WARM-UP SELESAI] Waktu eksekusi: {warmup_ms:.2f} ms (dibuang/tidak dicatat) <<<\n", flush=True)

    # ==========================================================================
    # 2. TAHAP PENGUJIAN UTAMA (DICATAT KE CSV)
    # ==========================================================================
    all_times = list(existing_times)
    new_results: List[Dict[str, Any]] = []

    with open(output_csv, mode=file_mode, newline="", encoding="utf-8") as f:
        # Tulis header jika membuat file baru
        if file_mode == "w":
            f.write("no|time (ms)|average\n")
            f.flush()

        for i in range(total_runs):
            current_no = start_no + i
            if total_runs > 1 or args.quiet:
                print(f"[Menjalankan Uji {i + 1}/{total_runs} (No. {current_no}) ...]", flush=True)

            elapsed_ms = run_single_test_ast(
                script_path=args.target,
                extra_args=extra_flags,
                quiet=args.quiet
            )

            all_times.append(elapsed_ms)
            running_avg = sum(all_times) / len(all_times)

            time_str = format_num(elapsed_ms, args.decimals)
            avg_str = format_num(running_avg, args.decimals)

            # Tulis baris baru langsung ke CSV
            f.write(f"{current_no}|{time_str}|{avg_str}\n")
            f.flush()

            new_results.append({
                "no": current_no,
                "time_ms": elapsed_ms,
                "time_str": time_str,
                "avg_str": avg_str
            })

            print(f"  Hasil No. {current_no:<3} -> Waktu: {time_str:>8} ms | Average: {avg_str:>8} ms", flush=True)

    # Ringkasan Pengujian
    times_only = [r["time_ms"] for r in new_results]
    mean_val = sum(times_only) / len(times_only)
    min_val = min(times_only)
    max_val = max(times_only)

    print("\n" + "=" * 70)
    print(f"  RINGKASAN PENGUJIAN SELESAI (N = {total_runs})")
    print("-" * 70)
    print(f"  Berkas CSV        : {output_csv}")
    print(f"  Iterasi Warm-up   : {num_warmup} kali (diabaikan/tidak dicatat)")
    print(f"  Baris Terdata     : {new_results[0]['no']} s.d. {new_results[-1]['no']} ({total_runs} data)")
    print(f"  Rata-rata Waktu   : {format_num(mean_val, args.decimals)} ms")
    print(f"  Waktu Tercepat    : {format_num(min_val, args.decimals)} ms")
    print(f"  Waktu Terlama     : {format_num(max_val, args.decimals)} ms")
    print("=" * 70)


if __name__ == "__main__":
    main()
