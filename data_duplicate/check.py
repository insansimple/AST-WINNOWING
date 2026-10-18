"""
Skrip Pemeriksa Duplikasi Berkas Dataset (check.py)
Lokasi: publikasi/analysis/data_duplicate/check.py

Tujuan:
Mengecek apakah terdapat berkas yang sama antara dataset uji (test.csv)
dan dataset penalaan (tunning.csv) untuk memastikan tidak terjadi kebocoran data (data leakage)
dan partisi berkas bersifat disjoint (saling lepas).

Pengecekan yang dilakukan:
1. Kesamaan Path Berkas (Normalized Relative Path): memeriksa apakah ada file dengan path yang sama.
2. Kesamaan Hash Konten (SHA-256): memeriksa apakah ada file dengan isi biner/teks yang identik,
   meskipun nama berkas atau foldernya berbeda.
3. Kesamaan Nama Berkas (Basename): memeriksa apakah ada nama file yang sama antar dataset.
4. Kesamaan Pasangan (Pair Match): memeriksa apakah ada pasangan kode (file1, file2) yang duplikat.

Hasil:
result.csv : Daftar berkas yang sama/duplikat (jika ada). Jika tidak ada duplikasi,
             file tetap dibuat dengan header kolom dan 0 baris data (bersih/disjoint).
"""

import argparse
import csv
import hashlib
import os
from pathlib import Path
from typing import List, Dict, Any, Set, Tuple


# Direktori dasar
CURRENT_DIR = Path(__file__).resolve().parent
ANALYSIS_DIR = CURRENT_DIR.parent
PUBLIKASI_DIR = ANALYSIS_DIR.parent
DATASET_DIR = PUBLIKASI_DIR / "dataset"

DEFAULT_TEST_CSV = DATASET_DIR / "test.csv"
DEFAULT_TUNNING_CSV = DATASET_DIR / "tunning.csv"
DEFAULT_OUTPUT_CSV = CURRENT_DIR / "result.csv"


def normalize_rel_path(path_str: str) -> str:
    """Membersihkan path relatif: hapus leading slashes dan ubah backslash ke forward slash."""
    clean = path_str.strip().lstrip("\\/").replace("\\", "/")
    return clean


def compute_sha256(file_path: Path) -> str:
    """Menghitung hash SHA-256 dari sebuah file."""
    if not file_path.exists():
        return ""
    hasher = hashlib.sha256()
    with file_path.open("rb") as f:
        while chunk := f.read(65536):
            hasher.update(chunk)
    return hasher.hexdigest()


def read_dataset_csv(csv_path: Path) -> List[Dict[str, Any]]:
    """Membaca berkas dataset CSV (otomatis mengenali delimiter | atau ,)."""
    if not csv_path.exists():
        raise FileNotFoundError(f"File dataset tidak ditemukan: {csv_path}")
    with csv_path.open("r", encoding="utf-8-sig", newline="") as f:
        sample = f.readline()
        delimiter = "|" if "|" in sample else ","
        f.seek(0)
        return list(csv.DictReader(f, delimiter=delimiter))


def extract_files_metadata(rows: List[Dict[str, Any]], base_dir: Path) -> Dict[str, Dict[str, Any]]:
    """
    Mengekstrak informasi seluruh file unik yang dirujuk dalam dataset (kolom file1 dan file2).
    Mengembalikan mapping: normalized_rel_path -> info metadata file.
    """
    file_map: Dict[str, Dict[str, Any]] = {}

    for row_idx, r in enumerate(rows, start=1):
        pair_id = r.get("no", r.get("id", str(row_idx)))
        kode_plag = r.get("kode_plag", "")

        for col in ["file1", "file2"]:
            raw_path = r.get(col, "")
            if not raw_path:
                continue

            rel_path = normalize_rel_path(raw_path)
            full_path = (base_dir / rel_path).resolve()

            if rel_path not in file_map:
                exists = full_path.exists()
                sha = compute_sha256(full_path) if exists else "FILE_NOT_FOUND"
                file_map[rel_path] = {
                    "rel_path": rel_path,
                    "file_name": Path(rel_path).name,
                    "full_path": str(full_path),
                    "exists": exists,
                    "sha256": sha,
                    "as_file1_count": 0,
                    "as_file2_count": 0,
                    "pairs": [],
                    "kategori": set()
                }

            if col == "file1":
                file_map[rel_path]["as_file1_count"] += 1
            else:
                file_map[rel_path]["as_file2_count"] += 1

            file_map[rel_path]["pairs"].append(f"Pair#{pair_id}({col})")
            if kode_plag:
                file_map[rel_path]["kategori"].add(kode_plag)

    return file_map


def check_duplicate_files(
    test_csv_path: Path,
    tunning_csv_path: Path,
    output_csv_path: Path,
    base_dir: Path = DATASET_DIR
) -> Dict[str, Any]:
    print("=" * 80)
    print("  PEMERIKSAAN DUPLIKASI BERKAS: TEST.CSV VS TUNNING.CSV")
    print(f"  Dataset Test    : {test_csv_path}")
    print(f"  Dataset Tunning : {tunning_csv_path}")
    print(f"  Direktori Basis : {base_dir}")
    print(f"  Output Hasil    : {output_csv_path}")
    print("=" * 80)

    # 1. Baca kedua berkas dataset
    test_rows = read_dataset_csv(test_csv_path)
    tune_rows = read_dataset_csv(tunning_csv_path)

    print(f"\n[1/4] Memuat data pasangan kode...")
    print(f"  - Pasangan pada test.csv    : {len(test_rows)} pasang")
    print(f"  - Pasangan pada tunning.csv : {len(tune_rows)} pasang")

    # 2. Ekstraksi seluruh file unik
    print(f"\n[2/4] Mengekstrak daftar berkas dan menghitung hash SHA-256...")
    test_files = extract_files_metadata(test_rows, base_dir)
    tune_files = extract_files_metadata(tune_rows, base_dir)

    print(f"  - Berkas unik pada test.csv    : {len(test_files)} berkas")
    print(f"  - Berkas unik pada tunning.csv : {len(tune_files)} berkas")

    # 3. Pengecekan overlap / duplikasi
    print(f"\n[3/4] Menganalisis potensi duplikasi (Path, Hash SHA-256, dan Nama Berkas)...")

    # 3a. Duplikasi berdasarkan Path Berkas yang persis sama
    path_overlap = set(test_files.keys()) & set(tune_files.keys())

    # 3b. Duplikasi berdasarkan Hash Konten SHA-256 yang identik
    test_hash_to_paths: Dict[str, List[str]] = {}
    for p, meta in test_files.items():
        if meta["sha256"] and meta["sha256"] != "FILE_NOT_FOUND":
            test_hash_to_paths.setdefault(meta["sha256"], []).append(p)

    tune_hash_to_paths: Dict[str, List[str]] = {}
    for p, meta in tune_files.items():
        if meta["sha256"] and meta["sha256"] != "FILE_NOT_FOUND":
            tune_hash_to_paths.setdefault(meta["sha256"], []).append(p)

    hash_overlap = set(test_hash_to_paths.keys()) & set(tune_hash_to_paths.keys())

    # 3c. Duplikasi berdasarkan Nama Berkas (Basename)
    test_names_to_paths: Dict[str, List[str]] = {}
    for p, meta in test_files.items():
        test_names_to_paths.setdefault(meta["file_name"], []).append(p)

    tune_names_to_paths: Dict[str, List[str]] = {}
    for p, meta in tune_files.items():
        tune_names_to_paths.setdefault(meta["file_name"], []).append(p)

    name_overlap = set(test_names_to_paths.keys()) & set(tune_names_to_paths.keys())

    # 4. Susun rekaman duplikasi untuk result.csv
    results_records: List[Dict[str, Any]] = []

    # Catat duplikasi path atau hash
    processed_pairs: Set[Tuple[str, str]] = set()

    for p in sorted(path_overlap):
        t_meta = test_files[p]
        u_meta = tune_files[p]
        processed_pairs.add((p, p))
        results_records.append({
            "file_path_test": p,
            "file_path_tunning": p,
            "file_name": t_meta["file_name"],
            "sha256": t_meta["sha256"],
            "match_type": "SAME_PATH_AND_CONTENT" if t_meta["sha256"] == u_meta["sha256"] else "SAME_PATH_DIFFERENT_CONTENT",
            "test_occurrences": f"file1:{t_meta['as_file1_count']}, file2:{t_meta['as_file2_count']}",
            "tunning_occurrences": f"file1:{u_meta['as_file1_count']}, file2:{u_meta['as_file2_count']}",
            "notes": f"Path identik ditemukan di kedua split. Kategori: Test={list(t_meta['kategori'])}, Tune={list(u_meta['kategori'])}"
        })

    # Tambahkan kasus hash sama namun path berbeda
    for h in sorted(hash_overlap):
        for tp in test_hash_to_paths[h]:
            for up in tune_hash_to_paths[h]:
                if (tp, up) not in processed_pairs:
                    processed_pairs.add((tp, up))
                    t_meta = test_files[tp]
                    u_meta = tune_files[up]
                    results_records.append({
                        "file_path_test": tp,
                        "file_path_tunning": up,
                        "file_name": f"{t_meta['file_name']} vs {u_meta['file_name']}",
                        "sha256": h,
                        "match_type": "SAME_CONTENT_DIFFERENT_PATH",
                        "test_occurrences": f"file1:{t_meta['as_file1_count']}, file2:{t_meta['as_file2_count']}",
                        "tunning_occurrences": f"file1:{u_meta['as_file1_count']}, file2:{u_meta['as_file2_count']}",
                        "notes": "Konten file identik (SHA-256 sama) meskipun path berbeda."
                    })

    # Tambahkan kasus nama file sama tetapi hash berbeda (jika ada)
    for fname in sorted(name_overlap):
        for tp in test_names_to_paths[fname]:
            for up in tune_names_to_paths[fname]:
                if (tp, up) not in processed_pairs:
                    t_meta = test_files[tp]
                    u_meta = tune_files[up]
                    if t_meta["sha256"] != u_meta["sha256"]:
                        processed_pairs.add((tp, up))
                        results_records.append({
                            "file_path_test": tp,
                            "file_path_tunning": up,
                            "file_name": fname,
                            "sha256": f"test:{t_meta['sha256'][:8]}... vs tune:{u_meta['sha256'][:8]}...",
                            "match_type": "SAME_FILENAME_ONLY",
                            "test_occurrences": f"file1:{t_meta['as_file1_count']}, file2:{t_meta['as_file2_count']}",
                            "tunning_occurrences": f"file1:{u_meta['as_file1_count']}, file2:{u_meta['as_file2_count']}",
                            "notes": "Nama file sama, namun isi/path berbeda."
                        })

    # Tulis hasil ke CSV
    output_csv_path.parent.mkdir(parents=True, exist_ok=True)
    fieldnames = [
        "file_path_test",
        "file_path_tunning",
        "file_name",
        "sha256",
        "match_type",
        "test_occurrences",
        "tunning_occurrences",
        "notes"
    ]

    with output_csv_path.open("w", encoding="utf-8", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(results_records)

    # Cetak ringkasan hasil
    print(f"\n[4/4] Hasil Pemeriksaan:")
    print("=" * 80)
    print(f"  1. Duplikasi Path Berkas (Exact Path)     : {len(path_overlap)} berkas")
    print(f"  2. Duplikasi Konten (SHA-256 Identik)    : {len(hash_overlap)} hash berkas")
    print(f"  3. Duplikasi Nama Berkas (Basename)       : {len(name_overlap)} nama berkas")
    print(f"  TOTAL REKAMAN DUPLIKASI                   : {len(results_records)} item")
    print("=" * 80)

    if len(results_records) == 0:
        print("\n>> KESIMPULAN: BERSIH / TIDAK ADA DUPLIKASI (DISJOINT)")
        print("   Tidak ditemukan satu pun berkas yang sama antara test.csv dan tunning.csv.")
        print("   Pembagian dataset memenuhi prinsip bebas data leakage.")
    else:
        print(f"\n>> PERINGATAN: Ditemukan {len(results_records)} berkas sama/duplikat!")
        for rec in results_records[:10]:
            print(f"   - [{rec['match_type']}] {rec['file_name']} -> {rec['file_path_test']}")
        if len(results_records) > 10:
            print(f"   ... dan {len(results_records) - 10} berkas lainnya.")

    print(f"\n[OK] Berkas hasil periksa disimpan ke: {output_csv_path}")

    return {
        "total_test_files": len(test_files),
        "total_tunning_files": len(tune_files),
        "path_overlap_count": len(path_overlap),
        "hash_overlap_count": len(hash_overlap),
        "name_overlap_count": len(name_overlap),
        "duplicate_records": results_records,
        "output_file": str(output_csv_path)
    }


def main():
    parser = argparse.ArgumentParser(
        description="Pemeriksa Duplikasi Berkas Antara test.csv dan tunning.csv"
    )
    parser.add_argument("--test", "-t", type=Path, default=DEFAULT_TEST_CSV,
                        help=f"Path ke test.csv (default: {DEFAULT_TEST_CSV})")
    parser.add_argument("--tunning", "-u", type=Path, default=DEFAULT_TUNNING_CSV,
                        help=f"Path ke tunning.csv (default: {DEFAULT_TUNNING_CSV})")
    parser.add_argument("--output", "-o", type=Path, default=DEFAULT_OUTPUT_CSV,
                        help=f"Path ke result.csv (default: {DEFAULT_OUTPUT_CSV})")
    parser.add_argument("--dataset-dir", "-d", type=Path, default=DATASET_DIR,
                        help=f"Direktori basis dataset (default: {DATASET_DIR})")

    args = parser.parse_args()

    check_duplicate_files(
        test_csv_path=args.test,
        tunning_csv_path=args.tunning,
        output_csv_path=args.output,
        base_dir=args.dataset_dir
    )


if __name__ == "__main__":
    main()
