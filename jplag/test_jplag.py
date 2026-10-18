"""Pengujian JPlag pada pasangan dataset/test.csv tanpa ketergantungan hasil AST."""
import argparse
import csv
import json
import shutil
import subprocess
import sys
import tempfile
import time
import zipfile
from pathlib import Path

CURRENT_DIR = Path(__file__).resolve().parent
ROOT = CURRENT_DIR.parent
sys.path.insert(0, str(ROOT / 'engine'))
from dataset_utils import (
    read_label, classification_metrics, get_category_desc,
    print_confusion_matrix, generate_confusion_matrix_md, generate_metrics_table_md
)

# ==============================================================================
# PENGATURAN PARAMETER DARI FROZEN CONFIG
# ==============================================================================
FROZEN_CONFIG_PATH = ROOT / 'frozen_config.json'
CONFIG = {}
if FROZEN_CONFIG_PATH.exists():
    with FROZEN_CONFIG_PATH.open('r', encoding='utf-8') as f:
        CONFIG = json.load(f)

THRESHOLD = float(CONFIG.get('jplag_threshold', 0.65))
RESULTS_DIR = CURRENT_DIR / 'results'
DEFAULT_CSV = RESULTS_DIR / 'jplag.csv'
DEFAULT_MD = RESULTS_DIR / 'jplag.md'
ARTIFACTS_DIR = CURRENT_DIR / 'artifacts'
# ==============================================================================


def evaluate_jplag(dataset=ROOT / 'dataset/test.csv', jar=CURRENT_DIR / 'jplag.jar',
                   output=DEFAULT_CSV, output_md=DEFAULT_MD, threshold=THRESHOLD,
                   artifacts_dir=ARTIFACTS_DIR):
    if isinstance(output_md, (float, int)):
        threshold = float(output_md)
        output_md = None

    dataset, jar, output = (Path(p).resolve() for p in (dataset, jar, output))
    if output_md is None:
        output_md = output.with_suffix('.md')
    else:
        output_md = Path(output_md).resolve()

    if not 0 <= threshold <= 1:
        raise ValueError('Threshold harus berada dalam rentang 0..1')
    output.parent.mkdir(parents=True, exist_ok=True)
    output_md.parent.mkdir(parents=True, exist_ok=True)

    with dataset.open(encoding='utf-8-sig', newline='') as stream:
        delimiter = '|' if '|' in stream.readline() else ','
        stream.seek(0)
        rows = list(csv.DictReader(stream, delimiter=delimiter))
    if not rows:
        raise ValueError('Dataset kosong')

    def source(value):
        return (dataset.parent / value.strip().lstrip('\\/').replace('\\', '/')).resolve()

    sources = sorted({source(row[key]) for row in rows for key in ('file1', 'file2')})
    mapping = {path: f's{i:04d}' for i, path in enumerate(sources)}
    artifacts = Path(artifacts_dir).resolve()
    artifacts.mkdir(parents=True, exist_ok=True)

    with tempfile.TemporaryDirectory(prefix='submissions_', dir=artifacts) as staging:
        for path, sid in mapping.items():
            dest = Path(staging) / sid
            dest.mkdir()
            shutil.copy2(path, dest / path.name)
        command = ['java', '-jar', str(jar), '-M', 'RUN', '-l', 'java', '--csv-export',
                   '--cluster-skip', '-n', '-1', '-m', '0', '--overwrite',
                   '-r', str(artifacts / 'results'), staging]
        start = time.perf_counter()
        with (artifacts / 'run.log').open('w', encoding='utf-8') as log:
            subprocess.run(command, stdout=log, stderr=subprocess.STDOUT, check=True)
        elapsed = time.perf_counter() - start

    with zipfile.ZipFile(artifacts / 'results.jplag') as archive:
        run = json.loads(archive.read('runInformation.json'))
        options = json.loads(archive.read('options.json'))
    failures = {r['submissionId']: r['submissionState'] for r in run['failedSubmissions']}
    with (artifacts / 'results/results.csv').open(encoding='utf-8-sig', newline='') as stream:
        scores = {tuple(sorted((r['submissionName1'], r['submissionName2']))): r
                  for r in csv.DictReader(stream)}

    details = []
    for row in rows:
        first, second = [mapping[source(row[key])] for key in ('file1', 'file2')]
        error = '; '.join(f'{sid}: {failures[sid]}' for sid in (first, second) if sid in failures)
        similarity = maximum = predicted = ''
        if not error:
            if first == second:
                similarity = maximum = 1.0
            else:
                result = scores.get(tuple(sorted((first, second))))
                if result is None:
                    raise ValueError(f'Perbandingan tidak ditemukan: {first}, {second}')
                similarity = float(result['averageSimilarity'])
                maximum = float(result['maxSimilarity'])
            predicted = int(similarity >= threshold)
        details.append(dict(id=row.get('no', row.get('id', '')), kode_plag=row.get('kode_plag', ''),
                            file1=row['file1'], file2=row['file2'], actual_label=read_label(row),
                            status='unavailable' if error else 'ok', error=error,
                            similarity=similarity,
                            similarity_percentage=round(similarity * 100, 2) if not error else '',
                            max_similarity=maximum, predicted_label=predicted, threshold=threshold))

    with output.open('w', encoding='utf-8', newline='') as stream:
        writer = csv.DictWriter(stream, fieldnames=list(details[0]))
        writer.writeheader()
        writer.writerows(details)

    valid = [r for r in details if r['status'] == 'ok']
    metrics = classification_metrics(valid)
    total_samples = len(details)
    avg_sim = (sum(r['similarity'] for r in valid) / len(valid)) if valid else 0.0
    total_time_ms = elapsed * 1000.0
    avg_time = (total_time_ms / total_samples) if total_samples > 0 else 0.0

    print("-" * 60)
    print("HASIL EVALUASI (RATA-RATA):")
    print(f"Total Sampel         : {total_samples}")
    print(f"Sampel Valid         : {len(valid)}")
    print(f"Rata-rata Kemiripan  : {avg_sim * 100:.2f}%")
    print(f"Rata-rata Waktu      : {avg_time:.3f} ms / pasangan kode (Batch)")
    print(f"Total Waktu Komputasi: {total_time_ms:.2f} ms ({elapsed:.2f} s)")
    print("=" * 60)

    # Cetak Confusion Matrix & Metrik Evaluasi di Konsol
    print_confusion_matrix(metrics, engine_name="JPlag")

    # Breakdown per kode_plag
    breakdown = {}
    for r in valid:
        k = r.get("kode_plag", "")
        if k not in breakdown:
            breakdown[k] = {"total": 0, "sims": []}
        breakdown[k]["total"] += 1
        breakdown[k]["sims"].append(r["similarity"])

    if len(breakdown) > 1:
        print("\nBREAKDOWN RATA-RATA PER KODE PLAGIARISME:")
        print(f"{'Kode Plag':<15} | {'Total':<6} | {'Rata Sim':<12} | {'Rata Waktu':<14}")
        print("-" * 55)
        for k, stats in sorted(breakdown.items()):
            tot = stats["total"]
            avg_sim_k = (sum(stats["sims"]) / tot) * 100 if tot > 0 else 0.0
            print(f"{k:<15} | {tot:<6} | {avg_sim_k:>10.2f}% | {'N/A (Batch)':>14}")
        print("=" * 55)

    print(f"\n[OK] Hasil detail CSV disimpan ke: {output}")

    # Simpan laporan Confusion Matrix & Evaluasi ke Markdown
    if output_md:
        md_lines = [
            "# Evaluasi Deteksi Plagiarisme Java: Engine JPlag\n",
            "Laporan pengujian kinerja dan akurasi deteksi kemiripan kode menggunakan **JPlag** (State-of-the-Art Academic Source Code Plagiarism Detection System).\n",
            "## 1. Parameter & Konfigurasi Pengujian\n",
            "| Parameter | Nilai |",
            "| :--- | :--- |",
            f"| **Dataset Pengujian** | `{dataset.name}` |",
            f"| **Jumlah Pasangan Kode** | `{total_samples}` pasangan |",
            f"| **Pasangan Tereksekusi (Valid)** | `{len(valid)}` pasangan |",
            f"| **Ambang Batas (Threshold)** | `{threshold:.2f}` (`{threshold*100:.0f}%`) |",
            "| **Bahasa Pemrograman** | `Java` |",
            f"| **Rata-rata Kemiripan** | `{avg_sim * 100:.2f}%` |",
            f"| **Rata-rata Waktu Eksekusi (Batch)** | `{avg_time:.3f} ms / pasangan` |",
            f"| **Total Waktu Pengujian** | `{total_time_ms:.2f} ms ({elapsed:.2f} s)` |\n",
            "## 2. Confusion Matrix\n",
            generate_confusion_matrix_md(metrics),
            "\n## 3. Metrik Evaluasi Klasifikasi\n",
            generate_metrics_table_md(metrics),
            "\n## 4. Breakdown Performa per Kategori Plagiarisme\n",
            "| Kode Plag | Deskripsi Kategori | Total Pasangan | Rata-rata Kemiripan (%) | Rata-rata Waktu (ms) |",
            "| :--- | :--- | :---: | :---: | :---: |"
        ]
        for k, stats in sorted(breakdown.items()):
            tot = stats["total"]
            avg_sim_k = (sum(stats["sims"]) / tot) * 100 if tot > 0 else 0.0
            desc = get_category_desc(k)
            md_lines.append(f"| `{k}` | {desc} | {tot} | {avg_sim_k:.2f}% | N/A (Batch) |")

        csv_rel_name = output.name if output else "jplag.csv"
        md_lines.extend([
            "\n## 5. Berkas Luaran Terkait\n",
            f"- Berkas CSV Hasil Detail: [`{csv_rel_name}`]({csv_rel_name})\n"
        ])
        with output_md.open("w", encoding="utf-8") as f:
            f.write("\n".join(md_lines))
        print(f"[OK] Laporan Confusion Matrix & Evaluasi disimpan ke: {output_md}")

    summary = dict(engine='JPlag', total_pairs=len(details), evaluated_pairs=len(valid),
                   unavailable_pairs=len(details)-len(valid),
                   avg_similarity=avg_sim,
                   metrics=metrics,
                   breakdown=breakdown,
                   batch_wall_seconds=elapsed,
                   timing_note='Waktu seluruh batch, termasuk JVM, parsing dan ekspor; bukan waktu per pasangan.',
                   run_information=run, options=options,
                   submissions={sid: str(path) for path, sid in mapping.items()})
    (artifacts / 'summary.json').write_text(json.dumps(summary, indent=2, default=str), encoding='utf-8')
    return summary


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--dataset', type=Path, default=ROOT / 'dataset/test.csv')
    parser.add_argument('--jar', type=Path, default=CURRENT_DIR / 'jplag.jar')
    parser.add_argument('--output', type=Path, default=DEFAULT_CSV,
                        help=f'Path output CSV (default: {DEFAULT_CSV})')
    parser.add_argument('--output-md', type=Path, default=DEFAULT_MD,
                        help=f'Path output Markdown (default: {DEFAULT_MD})')
    parser.add_argument('--threshold', type=float, default=THRESHOLD,
                        help=f'Threshold deteksi plagiarisme (default: {THRESHOLD})')
    args = parser.parse_args()
    evaluate_jplag(args.dataset, args.jar, args.output, args.output_md, args.threshold)
