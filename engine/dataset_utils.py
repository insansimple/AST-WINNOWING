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
    return dict(counts, Accuracy=100 * (tp + tn) / len(details) if details else 0.0,
                Precision=precision, Recall=recall,
                F1=2 * precision * recall / (precision + recall) if precision + recall else 0.0)
