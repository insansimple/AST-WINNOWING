"""Run local JPlag and compare only the pairs listed in dataset/test.csv."""
import argparse
import csv
import hashlib
import json
import shutil
import subprocess
import sys
import time
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / 'engine'))
from dataset_utils import read_label, classification_metrics


def read_csv(path, delimiter=','):
    with Path(path).open(encoding='utf-8-sig', newline='') as stream:
        return list(csv.DictReader(stream, delimiter=delimiter))


def write_csv(path, rows):
    with path.open('w', encoding='utf-8', newline='') as stream:
        writer = csv.DictWriter(stream, fieldnames=list(rows[0]))
        writer.writeheader()
        writer.writerows(rows)


def evaluate(dataset, jar, ast_results, output, threshold=0.65):
    dataset, jar, ast_results, output = map(lambda p: Path(p).resolve(), (dataset, jar, ast_results, output))
    output.mkdir(parents=True, exist_ok=True)
    rows = read_csv(dataset, '|')
    if not rows:
        raise ValueError('Empty dataset')
    paths = sorted({r[k].strip().lstrip('\\/') for r in rows for k in ('file1', 'file2')})
    mapping = {p: f's{i:04d}' for i, p in enumerate(paths)}
    # Isolate each run so removed/renamed submissions cannot leak into subsequent runs.
    import tempfile
    with tempfile.TemporaryDirectory(prefix='submissions_', dir=output) as staging:
        for rel, sid in mapping.items():
            src = dataset.parent / rel
            dest = Path(staging) / sid
            dest.mkdir()
            shutil.copy2(src, dest / src.name)
        command = ['java', '-jar', str(jar), '-M', 'RUN', '-l', 'java',
                   '--csv-export', '--cluster-skip', '-n', '-1', '-m', '0',
                   '--overwrite', '-r', str(output / 'results'), staging]
        start = time.perf_counter()
        with (output / 'jplag_run.log').open('w', encoding='utf-8') as log:
            subprocess.run(command, stdout=log, stderr=subprocess.STDOUT, check=True)
        wall_seconds = time.perf_counter() - start
    (output / 'submission_map.json').write_text(json.dumps(mapping, indent=2), encoding='utf-8')
    with zipfile.ZipFile(output / 'results.jplag') as archive:
        run = json.loads(archive.read('runInformation.json'))
        options = json.loads(archive.read('options.json'))
    failed = {r['submissionId']: r['submissionState'] for r in run['failedSubmissions']}
    comparisons = {tuple(sorted((r['submissionName1'], r['submissionName2']))): r
                   for r in read_csv(output / 'results/results.csv')}
    ast_rows = read_csv(ast_results)
    ast = {r['id']: r for r in ast_rows}
    if len(ast) != len(ast_rows):
        raise ValueError('Duplicate AST IDs')
    details, j_metrics_rows, a_metrics_rows = [], [], []
    for row in rows:
        sid = row.get('no', row.get('id'))
        actual = read_label(row)
        ar = ast[sid]
        if int(ar['actual_label']) != actual or ar['kode_plag'] != row['kode_plag']:
            raise ValueError(f'AST metadata mismatch for {sid}')
        if float(ar['threshold']) != threshold:
            raise ValueError('AST threshold differs; regenerate AST results with matching threshold')
        first, second = [mapping[row[k].strip().lstrip('\\/')] for k in ('file1', 'file2')]
        failure = '; '.join(f'{s}: {failed[s]}' for s in (first, second) if s in failed)
        score = maximum = pred = ''
        if not failure:
            key = tuple(sorted((first, second)))
            if first == second:
                score = maximum = 1.0
            elif key in comparisons:
                score = float(comparisons[key]['averageSimilarity'])
                maximum = float(comparisons[key]['maxSimilarity'])
            else:
                raise ValueError(f'Missing JPlag comparison {sid}: {key}')
            pred = int(score >= threshold)
            j_metrics_rows.append(dict(actual_label=actual, predicted_label=pred))
            a_metrics_rows.append(dict(actual_label=actual, predicted_label=int(ar['predicted_label'])))
        details.append(dict(id=sid, kode_plag=row['kode_plag'], file1=row['file1'], file2=row['file2'],
                            actual_label=actual, status='unavailable' if failure else 'ok', error=failure,
                            threshold=threshold, jplag_similarity=score, jplag_max_similarity=maximum,
                            jplag_pred=pred, ast_similarity=float(ar['similarity']),
                            ast_pred=int(ar['predicted_label']),
                            difference_jplag_minus_ast=score-float(ar['similarity']) if not failure else ''))
    write_csv(output / 'comparison_ast_jplag.csv', details)
    metrics = []
    for name, values in [('JPlag', j_metrics_rows), ('AST', a_metrics_rows)]:
        metrics.append(dict(engine=name, scope='common_valid_pairs', samples=len(values),
                            **classification_metrics(values)))
    metrics.append(dict(engine='AST', scope='all_test_pairs', samples=len(details),
                        **classification_metrics([dict(actual_label=r['actual_label'], predicted_label=r['ast_pred']) for r in details])))
    write_csv(output / 'metrics_ast_jplag.csv', metrics)
    categories = []
    for cat in sorted({r['kode_plag'] for r in details}):
        valid = [r for r in details if r['kode_plag']==cat and r['status']=='ok']
        for name, pred_key, score_key in [('JPlag','jplag_pred','jplag_similarity'),('AST','ast_pred','ast_similarity')]:
            values = [dict(actual_label=r['actual_label'], predicted_label=r[pred_key]) for r in valid]
            categories.append(dict(kode_plag=cat, engine=name, samples=len(valid),
                mean_similarity=sum(r[score_key] for r in valid)/len(valid) if valid else '',
                **classification_metrics(values)))
    write_csv(output / 'comparison_by_category.csv', categories)
    metadata = dict(command=command, jar_sha256=hashlib.sha256(jar.read_bytes()).hexdigest(),
                    dataset_sha256=hashlib.sha256(dataset.read_bytes()).hexdigest(),
                    ast_results_sha256=hashlib.sha256(ast_results.read_bytes()).hexdigest(),
                    run_information=run, options=options, wall_seconds=wall_seconds,
                    test_pairs=len(rows), valid_pairs=len(j_metrics_rows), unique_files=len(paths))
    (output / 'run_metadata.json').write_text(json.dumps(metadata, indent=2), encoding='utf-8')
    text = ['# Perbandingan JPlag dan AST', '',
            f'JPlag {run["version"]}; {len(rows)} pasangan test, {len(j_metrics_rows)} pasangan valid bersama.',
            f'Skor JPlag: averageSimilarity; threshold klasifikasi {threshold}. Minimum token: {options["minimumTokenMatch"]}.',
            'Normalisasi dan match merging JPlag tidak diaktifkan. Parameter JPlag tidak dituning pada test set.',
            'AST menggunakan hasil pengujian tersimpan (bobot 0,70/0,30). Skor kedua metode mengukur representasi berbeda.', '',
            '| Engine | Cakupan | N | Akurasi (%) | Presisi (%) | Recall (%) | F1 (%) | TP | FP | TN | FN |',
            '|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|']
    for r in metrics:
        text.append(f'| {r["engine"]} | {r["scope"]} | {r["samples"]} | {r["Accuracy"]:.2f} | {r["Precision"]:.2f} | {r["Recall"]:.2f} | {r["F1"]:.2f} | {r["TP"]} | {r["FP"]} | {r["TN"]} | {r["FN"]} |')
    text += ['', '## Pasangan tidak tersedia', '']
    for r in details:
        if r['status']!='ok': text.append(f'- ID {r["id"]}: {r["file1"]} vs {r["file2"]}; {r["error"]}.')
    text += ['', 'Pasangan yang tidak tersedia dikeluarkan dari metrik bersama; tidak dianggap skor nol atau prediksi negatif.',
             '', '## Waktu dan reproduksi', '',
             f'Wall time proses JPlag: {wall_seconds:.3f} detik, termasuk startup JVM, parsing, perbandingan, dan ekspor.',
             f'Waktu perbandingan internal JPlag: {run["executionTime"]} ms untuk {run["totalComparisons"]} pasangan batch.',
             'Waktu ini tidak dibandingkan langsung dengan AST per pasangan karena cakupan kerja dan caching berbeda.',
             'JPlag membandingkan semua berkas unik; laporan evaluasi hanya mengambil pasangan test.csv. Tidak memakai base-code exclusion.',
             'Opsi -m 0 menyimpan semua skor; threshold klasifikasi 0,65 diterapkan setelah ekspor.',
             'Jalankan ulang: python testing/test_jplag.py. Lihat run_metadata.json untuk opsi, versi, dan SHA-256 input.']
    (output / 'comparison_report.md').write_text('\n'.join(text)+'\n', encoding='utf-8')
    print(json.dumps(dict(metrics=metrics, valid_pairs=len(j_metrics_rows), excluded_pairs=len(rows)-len(j_metrics_rows)), indent=2))


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--dataset', type=Path, default=ROOT/'dataset/test.csv')
    parser.add_argument('--jar', type=Path, default=ROOT/'jplag/jplag.jar')
    parser.add_argument('--ast-results', type=Path, default=ROOT/'testing/test_ast_res.csv')
    parser.add_argument('--output', type=Path, default=ROOT/'jplag/evaluation')
    parser.add_argument('--threshold', type=float, default=0.65)
    args = parser.parse_args()
    evaluate(args.dataset, args.jar, args.ast_results, args.output, args.threshold)
