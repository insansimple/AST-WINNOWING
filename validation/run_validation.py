"""Reproducible group-disjoint split and evaluation; no performance-based split selection."""
import csv,hashlib,json,random,os,sys,contextlib,importlib.util,tempfile,io,platform,subprocess
from datetime import datetime, timezone
from collections import Counter,defaultdict
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/'validation/results'
sys.dont_write_bytecode=True
sys.path[:0]=[str(ROOT/'engine'),str(ROOT/'testing'),str(ROOT/'tunning')]
from dataset_utils import classification_metrics

def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
def write(path,rows,fields=None,delimiter=','):
 with path.open('w',encoding='utf-8',newline='') as f:
  w=csv.DictWriter(f,fieldnames=fields or list(rows[0]),delimiter=delimiter);w.writeheader();w.writerows(rows)
def split():
 OUT.mkdir(parents=True,exist_ok=True)
 with (ROOT/'dataset/dataset.csv').open(encoding='utf-8-sig') as f:rows=list(csv.DictReader(f,delimiter='|'))
 def src(v):return (ROOT/'dataset'/v.strip().lstrip('\\/').replace('\\','/')).resolve()
 files=sorted({src(r[k]) for r in rows for k in ('file1','file2')})
 parent={p:p for p in files}
 def find(p):
  if parent[p]!=p:parent[p]=find(parent[p])
  return parent[p]
 def union(a,b):
  a,b=find(a),find(b)
  if a!=b:parent[max(a,b)]=min(a,b)
 for r in rows:
  if r['label']=='1':union(src(r['file1']),src(r['file2']))
 hashes={}
 for p in files:
  if not p.exists() or not p.stat().st_size:raise ValueError(f'Missing/empty: {p}')
  h=sha(p)
  if h in hashes:union(p,hashes[h])
  hashes[h]=p
 groups=defaultdict(list)
 for p in files:groups[find(p)].append(p)
 positive={find(src(r['file1'])) for r in rows if r['label']=='1'}
 rng=random.Random(20261016);tuning=set()
 for stratum in [sorted(positive),sorted(set(groups)-positive)]:
  rng.shuffle(stratum);tuning.update(stratum[:round(.30*len(stratum))])
 allocation={g:'tunning' if g in tuning else 'test' for g in groups}
 outputs={'tunning':[],'test':[],'excluded_cross_partition':[]};used={k:set() for k in ['tunning','test']}
 for row in rows:
  a,b=src(row['file1']),src(row['file2']);pa,pb=allocation[find(a)],allocation[find(b)]
  target=pa if pa==pb else 'excluded_cross_partition'
  result=dict(row,file1=os.path.relpath(a,OUT).replace('\\','/'),file2=os.path.relpath(b,OUT).replace('\\','/'));outputs[target].append(result)
  if target in used:used[target].update((a,b))
 for name,rs in outputs.items():write(OUT/(name+'.csv'),rs,list(rows[0]),'|')
 assert not used['tunning']&used['test']
 assert not {sha(p) for p in used['tunning']}&{sha(p) for p in used['test']}
 assert len(rows)==sum(map(len,outputs.values()))
 assert Counter(r['no'] for r in rows)==Counter(r['no'] for rs in outputs.values() for r in rs)
 manifest=[dict(path=p.relative_to(ROOT).as_posix(),sha256=sha(p),bytes=p.stat().st_size,
                group=find(p).relative_to(ROOT).as_posix(),partition=allocation[find(p)]) for p in files]
 write(OUT/'manifest.csv',manifest)
 report=dict(seed=20261016,tuning_group_fraction=.3,group_count=len(groups),tuning_groups=len(tuning),test_groups=len(groups)-len(tuning),
  method='Connected components of positive pairs plus identical SHA-256. Single seeded shuffle in positive/control strata. No scores used to choose split.',
  provenance_limitation='Lineage inferred from labels; independent author verification pending.',
  exact_file_overlap=0,exact_content_overlap=0,
  partitions={k:dict(pairs=len(v),labels=dict(Counter(r['label'] for r in v)),categories=dict(Counter(r['kode_plag'] for r in v))) for k,v in outputs.items()},
  master_sha256=sha(ROOT/'dataset/dataset.csv'))
 (OUT/'split_audit.json').write_text(json.dumps(report,indent=2),encoding='utf-8')
 print(json.dumps(report,indent=2),flush=True)
 return manifest

def main():
 manifest=split()
 environment=dict(recorded_utc=datetime.now(timezone.utc).isoformat(),python=sys.version,platform=platform.platform(),machine=platform.machine(),processor=platform.processor(),java=subprocess.run(['java','-version'],capture_output=True,text=True).stderr,jar_sha256=sha(ROOT/'jplag/jplag.jar'))
 source_paths=[p for folder in ['engine','testing','tunning','jplag','validation'] for p in (ROOT/folder).glob('*.py') if p.name!='test_jplag.py' or p.parent.name=='jplag']
 environment['sources']={p.relative_to(ROOT).as_posix():sha(p) for p in sorted(source_paths)}
 (OUT.parent/'environment.json').write_text(json.dumps(environment,indent=2),encoding='utf-8')
 from tunning_ast import tune_ast_weights
 from tunning_winnowing import evaluate_parameter_grid
 from test_ast import evaluate_ast
 from test_winnowing import evaluate_winnowing
 from test_hybrid import evaluate_hybrid_effectiveness
 with contextlib.redirect_stdout(io.StringIO()):
  ast=tune_ast_weights(str(OUT/'tunning.csv'),str(OUT/'tuning_ast.csv'))['best_weights']
  winn=evaluate_parameter_grid(str(OUT/'tunning.csv'),[3,4,5,6,7,8,10],[2,3,4,5,6,8],str(OUT/'tuning_winnowing.csv'))['best_parameter']
 config=dict(k=winn['k'],w=winn['w'],weight_structural=ast['w_struct'],weight_subtree=ast['w_fp'],
             winnowing_threshold=.5,ast_threshold=.65,jplag_threshold=.65,lower_threshold=.15,upper_threshold=.70,
             threshold_policy='Existing thresholds fixed before this run; not optimized on new test. Prior full-dataset exposure remains a limitation.',
             input_hashes={n:sha(OUT/n) for n in ['tunning.csv','test.csv','manifest.csv']})
 (OUT/'frozen_config.json').write_text(json.dumps(config,indent=2),encoding='utf-8')
 print('Tuning complete:',json.dumps(config),flush=True)
 spec=importlib.util.spec_from_file_location('grouped_jplag',ROOT/'jplag/test_jplag.py');jp=importlib.util.module_from_spec(spec);spec.loader.exec_module(jp)
 test=str(OUT/'test.csv');ws=ast['w_struct'];wf=ast['w_fp'];k=winn['k'];w=winn['w']
 with tempfile.TemporaryDirectory(prefix='.jplag_work_',dir=OUT.parent) as scratch, contextlib.redirect_stdout(io.StringIO()):
  a=evaluate_ast(dataset_path=test,weight_structural=ws,weight_subtree=wf,output_result_csv=None)
  b=evaluate_winnowing(dataset_path=test,k=k,w=w,output_result_csv=None)
  h=evaluate_hybrid_effectiveness(dataset_path=test,k=k,w=w,weight_structural=ws,weight_subtree=wf,output_detail_csv=None)
  jp.evaluate_jplag(dataset=Path(test),output=Path(scratch)/'jplag.csv')
  with (Path(scratch)/'jplag.csv').open() as f:jr=list(csv.DictReader(f))
  metadata=json.loads((Path(scratch)/'jplag_artifacts/summary.json').read_text())
  metadata.pop('submissions',None)
  metadata['options'].pop('submissionDirectories',None)
  (OUT/'jplag_metadata.json').write_text(json.dumps(metadata,indent=2),encoding='utf-8')
 indexes=[{r['id']:r for r in records} for records in [a['details'],b['details'],h['details'],jr]]
 assert all(set(idx)==set(indexes[0]) for idx in indexes)
 details=[]
 for sid,ar in indexes[0].items():
  br,hr,j= [idx[sid] for idx in indexes[1:]]
  assert ar['actual_label']==br['actual_label']==hr['actual_label']==int(j['actual_label'])
  assert ar['predicted_label']==hr['ast_pred'] and br['predicted_label']==hr['winnowing_pred']
  details.append(dict(id=sid,kode_plag=ar['kode_plag'],actual_label=ar['actual_label'],
    ast_similarity=ar['similarity'],ast_pred=ar['predicted_label'],winnowing_similarity=br['similarity'],winnowing_pred=br['predicted_label'],
    hybrid_similarity=hr['hybrid_sim_pct']/100,hybrid_pred=hr['hybrid_pred'],jplag_similarity=j['similarity'],jplag_pred=j['predicted_label'],jplag_status=j['status']))
 write(OUT/'comparison.csv',details)
 valid=[r for r in details if r['jplag_status']=='ok'];metrics=[]
 for category in ['ALL']+sorted({r['kode_plag'] for r in valid}):
  subset=valid if category=='ALL' else [r for r in valid if r['kode_plag']==category]
  for engine in ['winnowing','ast','hybrid','jplag']:
   metrics.append(dict(category=category,engine=engine,samples=len(subset),**classification_metrics([dict(actual_label=r['actual_label'],predicted_label=int(r[engine+'_pred'])) for r in subset])))
 write(OUT/'metrics.csv',metrics)
 for r in manifest:assert sha(ROOT/r['path'])==r['sha256'],'Dataset changed during run'
 audit=json.loads((OUT/'split_audit.json').read_text())
 lines=['# Evaluasi dengan pemisahan kelompok program','',
  f"Kelompok tuning: {audit['tuning_groups']}; test: {audit['test_groups']}. Seed 20261016; target 30% kelompok tuning / 70% test.",
  'Kelompok dibentuk dari pasangan positif dan hash identik, kemudian diacak sekali dalam strata program positif/kontrol. Tidak memilih split berdasarkan skor.',
  'Pasangan negatif lintas partisi dikeluarkan dan dicatat, bukan dipindahkan diam-diam. Rasio pasangan tidak harus sama dengan rasio kelompok.', '',
  *[f"- {name}: {info['pairs']} pasangan; label {info['labels']}." for name,info in audit['partitions'].items()],
  '- Overlap berkas: 0; overlap SHA-256 isi: 0.','',
  f'Parameter hasil tuning: k={k}, w={w}, bobot AST={ws}/{wf}. Threshold tetap: Winnowing 0,50; AST/JPlag 0,65; hybrid 0,15/0,70.',
  'Konfigurasi disimpan sebelum evaluasi test. Default engine global tidak diubah.', '',
  '| Metode | N | Akurasi (%) | Presisi (%) | Recall (%) | F1 (%) | TP | FP | TN | FN |',
  '|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|']
 for r in metrics[:4]:lines.append(f"| {r['engine']} | {r['samples']} | {r['Accuracy']:.2f} | {r['Precision']:.2f} | {r['Recall']:.2f} | {r['F1']:.2f} | {r['TP']} | {r['FP']} | {r['TN']} | {r['FN']} |")
 lines+=['','## Batas kesimpulan','',
  f'Pasangan JPlag tidak tersedia: {len(details)-len(valid)}. Metrik tabel memakai pasangan valid bersama.',
  'Tidak ada overlap identitas/isi berdasarkan audit ini. Kemiripan semantik dan asal data yang tidak tercatat belum dapat dikesampingkan.',
  'Program dalam dataset telah diamati pada eksperimen sebelumnya. Hasil ini adalah evaluasi ulang dengan split kelompok, bukan dataset eksternal baru yang belum pernah diamati.',
  'Provenance masih berdasarkan label; verifikasi penulis diperlukan. JPlag memakai default minimum 9 token tanpa tuning; threshold tetap sebelumnya tidak dianggap hasil optimasi baru.',
  'Ablation, robustness, benchmark berulang, serta validasi dataset eksternal belum dicakup. Hasil tidak boleh digeneralisasikan ke semua program.',
  '', 'Reproduksi: python validation/run_validation.py. Dataset sumber berada di dataset/. Paket validation hanya memuat evaluasi split kelompok.']
 (OUT/'report.md').write_text('\n'.join(lines)+'\n',encoding='utf-8')
 print(json.dumps(metrics[:4],indent=2),flush=True)
if __name__=='__main__':main()
