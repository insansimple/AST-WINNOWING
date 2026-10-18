"""Aturan label bersama untuk tuning dan evaluasi."""

def read_label(row):
    """Label eksplisit diprioritaskan; kategori dipakai jika label kosong."""
    value = row.get("label")
    if value is not None and str(value).strip():
        value = str(value).strip()
        if value not in {"0", "1"}:
            raise ValueError(f"Label harus 0 atau 1, ditemukan: {value!r}")
        return int(value)
    category = str(row.get("kode_plag", "")).strip().upper()
    if category == "NON_PLAG":
        return 0
    if category.startswith("PLAG_"):
        return 1
    raise ValueError("Label tidak tersedia dan kode_plag tidak dikenali")


def classification_metrics(details):
    counts = {"TP": 0, "FP": 0, "TN": 0, "FN": 0}
    for row in details:
        actual, predicted = row["actual_label"], row["predicted_label"]
        key = ("TP" if actual else "FP") if predicted else ("FN" if actual else "TN")
        counts[key] += 1
    tp, fp, tn, fn = (counts[k] for k in ("TP", "FP", "TN", "FN"))
    precision = 100.0 * tp / (tp + fp) if tp + fp else 0.0
    recall = 100.0 * tp / (tp + fn) if tp + fn else 0.0
    specificity = 100.0 * tn / (tn + fp) if tn + fp else 0.0
    return dict(counts, Accuracy=100 * (tp + tn) / len(details) if details else 0.0,
                Precision=precision, Recall=recall, Specificity=specificity,
                F1=2 * precision * recall / (precision + recall) if precision + recall else 0.0)


CATEGORY_DESCRIPTIONS = {
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


def get_category_desc(kode_plag: str) -> str:
    """Mengembalikan deskripsi kategori plagiarisme yang informatif."""
    return CATEGORY_DESCRIPTIONS.get(kode_plag, kode_plag)


def print_confusion_matrix(metrics: dict, engine_name: str = "ENGINE") -> None:
    """Mencetak Confusion Matrix dan metrik klasifikasi dalam format tabel teks rapi."""
    tp = metrics.get("TP", 0)
    fp = metrics.get("FP", 0)
    tn = metrics.get("TN", 0)
    fn = metrics.get("FN", 0)
    p_total = tp + fn
    n_total = fp + tn
    pred_p = tp + fp
    pred_n = fn + tn
    total = p_total + n_total

    print("\n" + "=" * 75)
    print(f"  CONFUSION MATRIX & METRIK EVALUASI KLASIFIKASI: {engine_name.upper()}")
    print("=" * 75)
    print(f"                             |  Prediksi Plagiat (1)  |  Prediksi Non-Plag (0)  |  Total")
    print("-" * 75)
    print(f"  Aktual Plagiat (1)         |  {tp:>12} (TP)     |  {fn:>14} (FN)     |  {p_total:>5}")
    print(f"  Aktual Non-Plagiat (0)     |  {fp:>12} (FP)     |  {tn:>14} (TN)     |  {n_total:>5}")
    print("-" * 75)
    print(f"  Total Prediksi             |  {pred_p:>20}     |  {pred_n:>22}     |  {total:>5}")
    print("=" * 75)
    print("  Metrik Kinerja Klasifikasi:")
    print(f"  - Akurasi (Accuracy)       : {metrics.get('Accuracy', 0.0):>6.2f}%")
    print(f"  - Presisi (Precision)      : {metrics.get('Precision', 0.0):>6.2f}%")
    print(f"  - Recall / Sensitivitas    : {metrics.get('Recall', 0.0):>6.2f}%")
    print(f"  - Spesifisitas (TNR)       : {metrics.get('Specificity', 0.0):>6.2f}%")
    print(f"  - F1-Score                 : {metrics.get('F1', 0.0):>6.2f}%")
    print("=" * 75)


def generate_confusion_matrix_md(metrics: dict) -> str:
    """Menghasilkan representasi tabel Markdown untuk Confusion Matrix."""
    tp = metrics.get("TP", 0)
    fp = metrics.get("FP", 0)
    tn = metrics.get("TN", 0)
    fn = metrics.get("FN", 0)
    p_total = tp + fn
    n_total = fp + tn
    pred_p = tp + fp
    pred_n = fn + tn
    total = p_total + n_total

    return (
        "| Kondisi Aktual \\ Prediksi | Prediksi Plagiat (1) | Prediksi Non-Plagiat (0) | Total Aktual |\n"
        "| :--- | :---: | :---: | :---: |\n"
        f"| **Aktual Plagiat (1)** | **{tp}** (TP) | **{fn}** (FN) | **{p_total}** |\n"
        f"| **Aktual Non-Plagiat (0)** | **{fp}** (FP) | **{tn}** (TN) | **{n_total}** |\n"
        f"| **Total Prediksi** | **{pred_p}** | **{pred_n}** | **{total}** |\n"
    )


def generate_metrics_table_md(metrics: dict) -> str:
    """Menghasilkan representasi tabel Markdown untuk metrik performa klasifikasi."""
    acc = metrics.get("Accuracy", 0.0)
    prec = metrics.get("Precision", 0.0)
    rec = metrics.get("Recall", 0.0)
    spec = metrics.get("Specificity", 0.0)
    f1 = metrics.get("F1", 0.0)

    return (
        "| Metrik Evaluasi | Nilai (%) | Formula & Definisi |\n"
        "| :--- | :---: | :--- |\n"
        f"| **Akurasi (Accuracy)** | **{acc:.2f}%** | Rasio prediksi benar secara keseluruhan: `(TP + TN) / Total` |\n"
        f"| **Presisi (Precision)** | **{prec:.2f}%** | Ketepatan deteksi positif: `TP / (TP + FP)` |\n"
        f"| **Recall (Sensitivity)** | **{rec:.2f}%** | Jangkauan deteksi plagiarisme: `TP / (TP + FN)` |\n"
        f"| **Spesifisitas (Specificity)** | **{spec:.2f}%** | Ketepatan deteksi non-plagiat: `TN / (TN + FP)` |\n"
        f"| **F1-Score** | **{f1:.2f}%** | Rata-rata harmonik Presisi & Recall: `2 × (P × R) / (P + R)` |\n"
    )
