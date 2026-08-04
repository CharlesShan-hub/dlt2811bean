// 8.3.3 读逻辑节点目录：ACSI 类限定（标准表 26），选项与目录树圆点颜色一致
import { ACSI_DEFS } from '../acsiDefs.js'

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
    {
      key: 'acsi',
      label: '对象类型 acsiClass',
      type: 'select',
      options: ACSI_DEFS.map((a) => ({ value: a.key, label: `${a.label}（${a.key}）`, color: a.color })),
    },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
  ],
}
