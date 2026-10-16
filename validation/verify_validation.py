"""Independent checks of the grouped split and generated classifications."""
import csv,hashlib,json
from collections import Counter
from pathlib import Path
HERE=Path(__file__).resolve().parent/'results'
ROOT=HERE.parents[1]
def read(path,delimiter=','):
 with path.open(encoding='utf-8-sig',newline='') as f:return list(csv.DictReader(f,delimiter=delimiter))
def files(rows):return {(HERE/r[k]).resolve() for r in rows for k in ['file1','file2']}
def main():
 a=read(HERE/'tunning.csv','|');b=read(HERE/'test.csv','|');c=read(HERE/'excluded_cross_partition.csv','|')
 master=read(ROOT/'dataset/dataset.csv','|')
 assert Counter(r['no'] for r in a+b+c)==Counter(r['no'] for r in master)
 assert not files(a)&files(b)
 hashset=lambda ps:{hashlib.sha256(p.read_bytes()).hexdigest() for p in ps}
 assert not hashset(files(a))&hashset(files(b))
 group_rows=read(HERE/'manifest.csv')
 allocated={}
 for r in group_rows:
  allocated.setdefault(r['group'],set()).add(r['partition'])
 assert all(len(partitions)==1 for partitions in allocated.values())
 partitions={(ROOT/r['path']).resolve():r['partition'] for r in group_rows}
 for rows,name in [(a,'tunning'),(b,'test')]:
  for r in rows:assert {partitions[(HERE/r[k]).resolve()] for k in ['file1','file2']}=={name}
 for r in c:assert len({partitions[(HERE/r[k]).resolve()] for k in ['file1','file2']})==2
 assert all(r['label']=='0' for r in c)
 for r in group_rows:
  path=ROOT/r['path']
  assert path.stat().st_size==int(r['bytes'])
  assert hashlib.sha256(path.read_bytes()).hexdigest()==r['sha256']
 # Public dataset splits must resolve to the same pairs and labels as the frozen snapshots.
 def canonical(rows,base):
  return Counter((r['no'],r['kode_plag'],r['label'],*((base/r[k].replace(chr(92),'/').lstrip('/')).resolve().as_posix() for k in ['file1','file2'])) for r in rows)
 for rows,name in [(a,'tunning.csv'),(b,'test.csv')]:
  assert canonical(rows,HERE)==canonical(read(ROOT/'dataset'/name,'|'),ROOT/'dataset')
 audit=json.loads((HERE/'split_audit.json').read_text())
 assert hashlib.sha256((ROOT/'dataset/dataset.csv').read_bytes()).hexdigest()==audit['master_sha256']
 cfg=json.loads((HERE/'frozen_config.json').read_text())
 for n,d in cfg['input_hashes'].items():assert hashlib.sha256((HERE/n).read_bytes()).hexdigest()==d
 details=read(HERE/'comparison.csv');assert {r['id'] for r in details}=={r['no'] for r in b}
 for metric in read(HERE/'metrics.csv'):
  rows=[r for r in details if r['jplag_status']=='ok' and (metric['category']=='ALL' or r['kode_plag']==metric['category'])]
  counts=Counter()
  for r in rows:
   actual=int(r['actual_label']);pred=int(r[metric['engine']+'_pred'])
   counts[('TP' if actual else 'FP') if pred else ('FN' if actual else 'TN')]+=1
  assert int(metric['samples'])==len(rows)
  for key in ['TP','FP','TN','FN']:assert int(metric[key])==counts[key]
 print('PASS: full accounting, group/file/hash disjointness, excluded pairs, frozen inputs, test coverage and confusion matrices')
if __name__=='__main__':main()
