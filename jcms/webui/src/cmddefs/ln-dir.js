// 8.3.3 读逻辑节点目录：ACSI 类限定（标准表 26）
const acsiClasses = [
  { value: 1, label: 'DataObject', cn: '数据对象' },
  { value: 2, label: 'DATA-SET', cn: '数据集' },
  { value: 3, label: 'BRCB', cn: '缓存报告控制块' },
  { value: 4, label: 'URCB', cn: '非缓存报告控制块' },
  { value: 5, label: 'LCB', cn: '日志控制块' },
  { value: 6, label: 'LOG', cn: '日志' },
  { value: 7, label: 'SGCB', cn: '定值组控制块' },
  { value: 8, label: 'GoCB', cn: 'GOOSE 控制块' },
  { value: 10, label: 'MSVCB', cn: '多播采样值控制块' },
]

export default {
  title: '逻辑节点目录 ln-dir (8.3.3)',
  desc: '获取指定逻辑节点下的数据对象或控制块目录（GetLogicalNodeDirectory）',
  asn1: `ACSIClass ::= INTEGER {
    reserved       (0),
    data-object    (1),
    data-set       (2),
    brcb           (3),
    urcb           (4),
    lcb            (5),
    log            (6),
    sgcb           (7),
    gocb           (8),
    msvcb          (10)
} (0..10)

GetLogicalNodeDirectory-RequestPDU ::= SEQUENCE {
    reference       [0] IMPLICIT CHOICE {
        ldName        [0] IMPLICIT ObjectName,
        lnReference   [1] IMPLICIT ObjectReference
    },
    acsiClass       [1] IMPLICIT ACSIClass,
    referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL
}

GetLogicalNodeDirectory-ResponsePDU ::= SEQUENCE {
    reference       [0] IMPLICIT SEQUENCE OF SubReference,
    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetLogicalNodeDirectory-ErrorPDU ::= ServiceError — 8.3.3`,
  params: [
    { key: 'ln', label: '逻辑节点 ln', type: 'ln-select', placeholder: '选择逻辑节点（必填）', required: true },
    { key: 'acsi', label: '对象类型 acsiClass', type: 'select', options: acsiClasses.map((a) => `${a.value}: ${a.cn}（${a.label}）`) },
    { key: 'after', label: '起始引用 after', type: 'text', placeholder: '可选：上次响应的最后一个引用' },
  ],
}
