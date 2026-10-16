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
from dataset_utils import read_label, classification_metrics

# ==============================================================================
# PENGATURAN PARAMETER DARI FROZEN CONFIG
# ==============================================================================
FROZEN_CONFIG_PATH = ROOT / 'frozen_config.json'
CONFIG = {}
if FROZEN_CONFIG_PATH.exists():
    with FROZEN_CONFIG_PATH.open('r', encoding='utf-8') as f:
        CONFIG = json.load(f)

THRESHOLD = float(CONFIG.get('jplag_threshold', 0.65))
# ==============================================================================


def evaluate_jplag(dataset=ROOT / 'dataset/test.csv', jar=CURRENT_DIR / 'jplag.jar',
                   output=CURRENT_DIR / 'jplag_res.csv', threshold=THRESHOLD):
    dataset, jar, output = (Path(p).resolve() for p in (dataset, jar, output))
    if not 0 <= threshold <= 1:
        raise ValueError('Threshold harus berada dalam rentang 0..1')
    output.parent.mkdir(parents=True, exist_ok=True)
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
    artifacts = output.parent / (output.stem + '_artifacts')
    artifacts.mkdir(exist_ok=True)
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
    summary = dict(total_pairs=len(details), evaluated_pairs=len(valid), unavailable_pairs=len(details)-len(valid),
                   metrics=classification_metrics(valid), batch_wall_seconds=elapsed,
                   timing_note='Waktu seluruh batch, termasuk JVM, parsing dan ekspor; bukan waktu per pasangan.',
                   run_information=run, options=options,
                   submissions={sid: str(path) for path, sid in mapping.items()})
    (artifacts / 'summary.json').write_text(json.dumps(summary, indent=2), encoding='utf-8')
    print(f'Hasil: {output}')
    print(f'Pasangan: {len(details)}, valid: {len(valid)}, tidak tersedia: {len(details)-len(valid)}')
    print(json.dumps(summary['metrics'], indent=2))
    return summary


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--dataset', type=Path, default=ROOT / 'dataset/test.csv')
    parser.add_argument('--jar', type=Path, default=CURRENT_DIR / 'jplag.jar')
    parser.add_argument('--output', type=Path, default=CURRENT_DIR / 'jplag_res.csv')
    parser.add_argument('--threshold', type=float, default=THRESHOLD,
                        help=f'Threshold deteksi plagiarisme (default: {THRESHOLD})')
    args = parser.parse_args()
    evaluate_jplag(args.dataset, args.jar, args.output, args.threshold)
