import csv,json,re
from pathlib import Path
import xml.etree.ElementTree as ET

ROOT=Path(__file__).resolve().parents[2]/'samples/obix-project'
OUT=Path('outputs/impact-analysis');OUT.mkdir(parents=True,exist_ok=True)
def read(p):return p.read_text(encoding='utf-8-sig',errors='replace')
def rel(p):return p.relative_to(ROOT).as_posix()
def line(s,pos):return s.count('\n',0,pos)+1
def strip(s):
    return re.sub(r'/\*.*?\*/|//[^\n]*|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'',lambda m:re.sub(r'[^\n]',' ',m[0]),s,flags=re.S)
def endpair(s,start,a='{',b='}'):
    depth=0
    for i in range(start,len(s)):
        if s[i]==a:depth+=1
        elif s[i]==b:
            depth-=1
            if not depth:return i
    return len(s)-1
ddl=read(Path(__file__).resolve().parents[2]/'samples/3_DDL.ddl')
tables={m[1]:{'line':line(ddl,m.start()),'name':m[1]} for m in re.finditer(r'CREATE TABLE `([^`]+)`',ddl)}
fks=[]
for m in re.finditer(r'ALTER TABLE `([^`]+)`\s+ADD CONSTRAINT `([^`]+)`[^;]*?FOREIGN KEY\s*\((.*?)\)\s*REFERENCES `([^`]+)`\s*\((.*?)\)',ddl,re.S):
    fks.append(dict(child=m[1],constraint=m[2],child_columns=re.findall(r'`([^`]+)`',m[3]),parent=m[4],parent_columns=re.findall(r'`([^`]+)`',m[5]),line=line(ddl,m.start())))
classes={};methods={};parse_errors=[]
method_pattern=r'\b(?:public|protected|private)\s+(?:(?:static|final|synchronized|abstract)\s+)*(?:[\w<>\[\],?.]+\s+)+(\w+)\s*\('
for p in ROOT.rglob('*.java'):
    s=read(p);mask=strip(s);pkg=re.search(r'package\s+([\w.]+)',s)
    if not pkg:continue
    fq=pkg[1]+'.'+p.stem
    imports={m[1].split('.')[-1]:m[1] for m in re.finditer(r'import\s+([\w.]+);',s)}
    fields={m[2]:imports.get(m[1],pkg[1]+'.'+m[1]) for m in re.finditer(r'\b(?:private|protected|public)\s+(?:final\s+|static\s+)*([A-Z]\w*)\s+(\w+)\s*[;=]',mask)}
    wildcards=re.findall(r'import\s+([\w.]+)\.\*;',s)
    c={'path':rel(p),'fq':fq,'fields':fields,'text':s,'mask':mask,'imports':imports,'wildcards':wildcards,'methods':[]};classes[fq]=c
    previous=0
    for m in re.finditer(method_pattern,mask):
        par=mask.find('(',m.start());pend=endpair(mask,par,'(',')');tail=re.match(r'\s*(?:throws\s+[\w.,\s]+)?\s*([;{])',mask[pend+1:])
        if not tail or tail[1]!='{':continue
        start=pend+1+tail.end()-1;end=endpair(mask,start);header=s[previous:m.start()];previous=end+1
        annotations=header[header.rfind('}')+1:]
        routes=[]
        for annotation in re.finditer(r'@(?:Request|Get|Post|Put|Delete|Patch)Mapping\s*\((.*?)\)',annotations,re.S):routes+=re.findall(r'"([^"\n]+)"',annotation[1])[:1]
        routes=routes[-1:]
        summary=re.findall(r'@Operation\s*\(summary\s*=\s*"([^"]+)"',annotations)
        key=fq+'.'+m[1]
        calls=[{'variable':x[1],'method':x[2],'line':line(s,start+x.start())} for x in re.finditer(r'\b(\w+)\s*\.\s*(\w+)\s*\(',mask[start:end])]
        methods[key]={'key':key,'class':fq,'method':m[1],'path':rel(p),'line':line(s,m.start()),'end_line':line(s,end),'calls':calls,'routes':routes,'summary':summary[-1] if summary else '', 'body':s[start:end]}
        c['methods'].append(key)

sqls={};sql_files=[]
for p in ROOT.rglob('*_SQL.xml'):
    s=read(p)
    try:doc=ET.fromstring(s)
    except ET.ParseError as e:parse_errors.append({'path':rel(p),'error':str(e)});continue
    ns=doc.attrib.get('namespace','');module=p.relative_to(ROOT).parts[0];dialect='oracle' if '/oracle/' in rel(p) else 'mysql' if '/mysql/' in rel(p) else 'other'
    for el in doc:
        if el.tag not in ('sql','select','insert','update','delete'):continue
        ident=el.attrib.get('id');text=' '.join(el.itertext());clean=re.sub(r'/\*.*?\*/|--[^\n]*',' ',text,flags=re.S)
        used=sorted(set(t for t in re.findall(r'\b(?:FROM|JOIN|UPDATE|INTO)\s+`?([A-Z][A-Z0-9_]*)',clean,re.I) if t.upper() in tables))
        match=re.search(r'<'+el.tag+r'\s+id\s*=\s*["\']'+re.escape(ident)+r'["\']',s)
        key=(module,dialect,ns,ident)
        sqls[key]={'path':rel(p),'line':line(s,match.start()) if match else 1,'namespace':ns,'id':ident,'kind':el.tag,'tables':used,'includes':[x.attrib['refid'] for x in el.iter('include')], 'module':module,'dialect':dialect}
def expanded(key,seen=None):
    seen=set() if seen is None else seen
    if key in seen or key not in sqls:return set(),[]
    seen.add(key);obj=sqls[key];used=set(obj['tables']);evidence=[obj]
    for ref in obj['includes']:
        namespace,ident=ref.rsplit('.',1) if '.' in ref else (key[2],ref)
        other=(key[0],key[1],namespace,ident)
        sub,ev=expanded(other,seen);used|=sub;evidence+=ev
    return used,evidence
edges={}
unresolved=[]
for key,m in methods.items():
    c=classes[m['class']];resolved=[]
    for call in m['calls']:
        target=c['fields'].get(call['variable']) or c['imports'].get(call['variable'])
        if not target:continue
        if target not in classes:
            name=target.split('.')[-1]
            choices=[prefix+'.'+name for prefix in c['wildcards'] if prefix+'.'+name in classes]
            if len(choices)==1:target=choices[0]
        target_key=target+'.'+call['method']
        if target_key in methods:resolved.append((target_key,call['line']))
        module=m['path'].split('/')[0]
        for dialect in ('mysql','oracle','other'):
            sk=(module,dialect,target,call['method'])
            if sk in sqls:resolved.append((sk,call['line']))
    # Calls within the same class, including helpers used by public methods.
    for other in c['methods']:
        if other==key:continue
        pattern=r'(?<![\w.])(?:this\.)?'+re.escape(methods[other]['method'])+r'\s*\('
        hit=re.search(pattern,strip(m['body']))
        if hit:resolved.append((other,m['line']+m['body'][:hit.start()].count('\n')))
    edges[key]=resolved
def trace(key,seen=None):
    seen=set() if seen is None else seen
    if key in seen:return set(),[]
    seen.add(key);used=set();evidence=[]
    for target,call_line in edges.get(key,[]):
        if isinstance(target,tuple):
            ts,ev=expanded(target);used|=ts
            evidence.append({'caller':key,'call_line':call_line,'sql':sqls[target],'tables':sorted(ts),'fragments':[e['id'] for e in ev[1:]]})
        else:
            ts,ev=trace(target,seen);used|=ts;evidence+=ev
    return used,evidence
screens=[]
for p in ROOT.rglob('*.jsp'):
    if '/WEB-INF/jsp/admin/' not in rel(p):continue
    s=read(p);title=re.search(r'화면명\s*:\s*([^\n\r]+)',s)
    refs=[{'route':m[1],'line':line(s,m.start())} for m in re.finditer(r'["\'`]([^"\'`\s]*[A-Z][A-Z0-9]+\.(?:json|view|frag|excl))["\'`]',s)]
    screens.append({'path':rel(p),'id':p.stem,'title':title[1].strip() if title else p.stem,'requests':refs})
chains=[]
for key,m in methods.items():
    if not m['class'].endswith('Controller'):continue
    used,evidence=trace(key)
    for route in m['routes']:
        matches=[{'path':s['path'],'title':s['title'],'line':r['line']} for s in screens for r in s['requests'] if r['route'].split('/')[-1]==route.split('/')[-1]]
        cls=classes[m['class']];head=cls['text'].split('public class',1)[0]
        prefixes=re.findall(r'@RequestMapping\s*\([^)]*?"([^"]+)"',head,re.S)
        full_route=(prefixes[-1].rstrip('/') if prefixes else '')+'/'+route.lstrip('/')
        chains.append({'controller':key,'path':m['path'],'line':m['line'],'route':route,'full_route':full_route,'summary':m['summary'],'tables':sorted(used),'screens':matches,'evidence':evidence})
all_sql=[{**v,'tables':sorted(expanded(k)[0])} for k,v in sqls.items() if v['kind']!='sql']
payload={'root':str(ROOT),'tables':list(tables.values()),'foreign_keys':fks,'screens':screens,'chains':chains,'sql_statements':all_sql,'xml_errors':parse_errors}
(OUT/'relations.json').write_text(json.dumps(payload,ensure_ascii=False,indent=2),encoding='utf-8')
with (OUT/'screen-table-map.csv').open('w',encoding='utf-8-sig',newline='') as out:
    writer=csv.writer(out);writer.writerow(['화면명','화면 파일','화면 호출 줄','요청','컨트롤러','컨트롤러 줄','요청 기능','관련 테이블','SQL 근거'])
    for c in chains:
        if not c['tables']:continue
        for screen in c['screens'] or [{'title':'직접 호출 화면 미확인','path':'','line':''}]:
            writer.writerow([screen['title'],screen['path'],screen['line'],c['route'],c['path'],c['line'],c['summary'],', '.join(c['tables']),'; '.join(sorted(set(e['sql']['path']+':'+str(e['sql']['line'])+'#'+e['sql']['id'] for e in c['evidence'])))])
summary={'tables':len(tables),'foreign_keys':len(fks),'java_classes':len(classes),'java_methods':len(methods),'mapper_statements':len(all_sql),'admin_screens':len(screens),'controller_routes':len(chains),'routes_with_table_trace':sum(bool(c['tables']) for c in chains),'xml_errors':parse_errors}
(OUT/'inventory.json').write_text(json.dumps(summary,ensure_ascii=False,indent=2),encoding='utf8')
print(json.dumps(summary,ensure_ascii=False))
for c in chains:
    if c['tables'] and c['screens']:print(c['route'],c['summary'],'=>',','.join(c['tables']),'|',','.join(sorted(set(x['title'] for x in c['screens']))))
