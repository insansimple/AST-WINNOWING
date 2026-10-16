"""
Skrip Pembuat Perbandingan dan Metrik Evaluasi Komparatif (make_comparison.py)

Menggabungkan hasil evaluasi dari 4 engine deteksi plagiarisme:
1. Winnowing (test_winn_rest.csv)
2. AST (test_ast_res.csv)
3. Hybrid Pipeline (test_hybrid.csv)
4. JPlag (jplag_res.csv)

Menghasilkan:
1. comparison.csv : Perbandingan skor kemiripan dan prediksi per pasangan sampel kode.
2. metrics.csv    : Ringkasan metrik klasifikasi (TP, FP, TN, FN, Accuracy, Precision, Recall, F1)
                    secara global ('ALL') dan rincian per kategori kode_plag.
"""

import argparse
import csv
from pathlib import Path
import sys
from typing import List, Dict, Any

# Path default relatif terhadap lokasi file ini
CURRENT_DIR = Path(__file__).resolve().parent
PUBLIKASI_DIR = CURRENT_DIR.parent
ENGINE_DIR = PUBLIKASI_DIR / "engine"

if str(ENGINE_DIR) not in sys.path:
    sys.path.insert(0, str(ENGINE_DIR))

try:
    from dataset_utils import classification_metrics
except ImportError:
    def classification_metrics(details: List[Dict[str, Any]]) -> Dict[str, Any]:
        counts = {"TP": 0, "FP": 0, "TN": 0, "FN": 0}
        for row in details:
            actual, predicted = row["actual_label"], row["predicted_label"]
            key = ("TP" if actual else "FP") if predicted else ("FN" if actual else "TN")
            counts[key] += 1
        tp, fp, tn, fn = (counts[k] for k in ("TP", "FP", "TN", "FN"))
        precision = 100.0 * tp / (tp + fp) if tp + fp else 0.0
        recall = 100.0 * tp / (tp + fn) if tp + fn else 0.0
        accuracy = 100.0 * (tp + tn) / len(details) if details else 0.0
        f1 = 2 * precision * recall / (precision + recall) if precision + recall else 0.0
        return dict(counts, Accuracy=accuracy, Precision=precision, Recall=recall, F1=f1)


def read_csv(path: Path) -> List[Dict[str, Any]]:
    if not path.exists():
        raise FileNotFoundError(f"File tidak ditemukan: {path}")
    with path.open("r", encoding="utf-8-sig", newline="") as stream:
        sample = stream.readline()
        delimiter = "|" if "|" in sample else ","
        stream.seek(0)
        return list(csv.DictReader(stream, delimiter=delimiter))


def write_csv(path: Path, rows: List[Dict[str, Any]]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    if not rows:
        return
    with path.open("w", encoding="utf-8", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=list(rows[0].keys()))
        writer.writeheader()
        writer.writerows(rows)


def build_comparison(
    winn_path: Path,
    ast_path: Path,
    hybrid_path: Path,
    jplag_path: Path,
    output_dir: Path
) -> Dict[str, Any]:
    print("=" * 80)
    print("  MEMBUAT DATA PERBANDINGAN DAN METRIK EVALUASI KOMPARATIF")
    print(f"  Winnowing CSV : {winn_path}")
    print(f"  AST CSV        : {ast_path}")
    print(f"  Hybrid CSV     : {hybrid_path}")
    print(f"  JPlag CSV      : {jplag_path}")
    print(f"  Output Dir     : {output_dir}")
    print("=" * 80)

    # 1. Baca seluruh file hasil evaluasi
    winn_rows = read_csv(winn_path)
    ast_rows = read_csv(ast_path)
    hybrid_rows = read_csv(hybrid_path)
    jplag_rows = read_csv(jplag_path)

    # Indeks berdasarkan ID pasangan
    winn_idx = {r["id"]: r for r in winn_rows}
    ast_idx = {r["id"]: r for r in ast_rows}
    hybrid_idx = {r["id"]: r for r in hybrid_rows}
    jplag_idx = {r["id"]: r for r in jplag_rows}

    # Pastikan seluruh dataset memiliki himpunan ID yang konsisten
    ids_order = [r["id"] for r in ast_rows]
    for name, idx in [("Winnowing", winn_idx), ("Hybrid", hybrid_idx), ("JPlag", jplag_idx)]:
        missing = set(ids_order) - set(idx.keys())
        if missing:
            raise ValueError(f"Terdapat ID pada AST yang tidak ditemukan di {name}: {missing}")

    # 2. Susun tabel comparison.csv
    comparison_details = []
    for sid in ids_order:
        ar = ast_idx[sid]
        wr = winn_idx[sid]
        hr = hybrid_idx[sid]
        jr = jplag_idx[sid]

        actual_label = int(ar["actual_label"])

        # Skor kemiripan dalam skala 0.0 - 1.0
        ast_sim = float(ar["similarity"])
        ast_pred = int(ar["predicted_label"])

        winn_sim = float(wr["similarity"])
        winn_pred = int(wr["predicted_label"])

        # Hybrid similarity dikonversi dari hybrid_sim_pct (%) ke desimal [0..1]
        hybrid_sim = float(hr["hybrid_sim_pct"]) / 100.0
        hybrid_pred = int(hr["hybrid_pred"])

        # JPlag similarity dan status
        j_status = jr.get("status", "ok")
        j_sim = float(jr["similarity"]) if jr.get("similarity", "") != "" else ""
        j_pred = int(jr["predicted_label"]) if jr.get("predicted_label", "") != "" else ""

        comparison_details.append({
            "id": sid,
            "kode_plag": ar["kode_plag"],
            "actual_label": actual_label,
            "ast_similarity": ast_sim,
            "ast_pred": ast_pred,
            "winnowing_similarity": winn_sim,
            "winnowing_pred": winn_pred,
            "hybrid_similarity": hybrid_sim,
            "hybrid_pred": hybrid_pred,
            "jplag_similarity": j_sim,
            "jplag_pred": j_pred,
            "jplag_status": j_status
        })

    comp_file = output_dir / "comparison.csv"
    write_csv(comp_file, comparison_details)
    print(f"\n[OK] comparison.csv berhasil disimpan: {comp_file} ({len(comparison_details)} baris)")

    # 3. Susun tabel metrics.csv
    # Hanya pasangan dengan status JPlag 'ok' yang dihitung dalam komparasi 4-engine
    valid = [r for r in comparison_details if r["jplag_status"] == "ok"]
    unique_categories = sorted({r["kode_plag"] for r in valid})
    categories = ["ALL"] + unique_categories

    metrics_rows = []
    engines = ["winnowing", "ast", "hybrid", "jplag"]

    for category in categories:
        subset = valid if category == "ALL" else [r for r in valid if r["kode_plag"] == category]
        samples_count = len(subset)

        for engine in engines:
            metric_input = [
                {"actual_label": r["actual_label"], "predicted_label": int(r[f"{engine}_pred"])}
                for r in subset
            ]
            m = classification_metrics(metric_input)
            metrics_rows.append({
                "category": category,
                "engine": engine,
                "samples": samples_count,
                "TP": m["TP"],
                "FP": m["FP"],
                "TN": m["TN"],
                "FN": m["FN"],
                "Accuracy": m["Accuracy"],
                "Precision": m["Precision"],
                "Recall": m["Recall"],
                "F1": m["F1"]
            })

    metrics_file = output_dir / "metrics.csv"
    write_csv(metrics_file, metrics_rows)
    print(f"[OK] metrics.csv berhasil disimpan: {metrics_file} ({len(metrics_rows)} baris)")

    # 4. Cetak tabel ringkasan performa global ('ALL')
    print("\n" + "=" * 90)
    print("   RINGKASAN METRIK PERFORMA KESELURUHAN (KATEGORI: ALL, SAMPEL: %d)" % len(valid))
    print("=" * 90)
    print(f"{'Engine':<12} | {'TP':<4} | {'FP':<4} | {'TN':<4} | {'FN':<4} | {'Akurasi (%)':<12} | {'Presisi (%)':<12} | {'Recall (%)':<11} | {'F1-Score (%)':<12}")
    print("-" * 90)
    for r in metrics_rows:
        if r["category"] == "ALL":
            print(f"{r['engine']:<12} | {r['TP']:<4} | {r['FP']:<4} | {r['TN']:<4} | {r['FN']:<4} | "
                  f"{r['Accuracy']:>10.2f}% | {r['Precision']:>10.2f}% | {r['Recall']:>9.2f}% | {r['F1']:>11.2f}%")
    print("=" * 90)

    return {
        "comparison": comparison_details,
        "metrics": metrics_rows
    }



def main():
    default_winn = PUBLIKASI_DIR / "testing" / "test_winn_rest.csv"
    default_ast = PUBLIKASI_DIR / "testing" / "test_ast_res.csv"
    default_hyb = PUBLIKASI_DIR / "testing" / "test_hybrid.csv"
    default_jplag = PUBLIKASI_DIR / "jplag" / "jplag_res.csv"
    default_out = CURRENT_DIR

    parser = argparse.ArgumentParser(
        description="Pembuat berkas perbandingan dan metrik evaluasi komparatif dari 4 engine plagiarisme."
    )
    parser.add_argument("--winnowing", "-w", type=Path, default=default_winn,
                        help=f"Path ke test_winn_rest.csv (default: {default_winn})")
    parser.add_argument("--ast", "-a", type=Path, default=default_ast,
                        help=f"Path ke test_ast_res.csv (default: {default_ast})")
    parser.add_argument("--hybrid", "-y", type=Path, default=default_hyb,
                        help=f"Path ke test_hybrid.csv (default: {default_hyb})")
    parser.add_argument("--jplag", "-j", type=Path, default=default_jplag,
                        help=f"Path ke jplag_res.csv (default: {default_jplag})")
    parser.add_argument("--output-dir", "-o", type=Path, default=default_out,
                        help=f"Direktori output CSV (default: {default_out})")

    args = parser.parse_args()

    build_comparison(
        winn_path=args.winnowing,
        ast_path=args.ast,
        hybrid_path=args.hybrid,
        jplag_path=args.jplag,
        output_dir=args.output_dir
    )


if __name__ == "__main__":
    main()
