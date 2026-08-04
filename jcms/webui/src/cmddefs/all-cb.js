// 8.3.6 读所有控制块值（GetAllCBValues），acsi 下拉与目录树圆点颜色一致
import { ACSI_DEFS } from '../acsiDefs.js'

const CB_KEYS = ['brcb', 'urcb', 'lcb', 'sgcb', 'gocb', 'msvcb']

export default {
  title: '读所有控制块值 all-cb (8.3.6)',
  desc: '获取指定逻辑设备或逻辑节点下所有控制块的值（GetAllCBValues）',
  asn1: `GetAllCBValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT CHOICE {
        ldName         [0] IMPLICIT ObjectName,
        lnReference    [1] IMPLICIT ObjectReference
    },
    acsiClass        [1] IMPLICIT ACSIClass,
    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
}

GetAllCBValues-ResponsePDU ::= SEQUENCE {
    cbValue          [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT SubReference,
        value         [1] IMPLICIT CHOICE {
            brcb        [0] IMPLICIT BRCB,
            urcb        [1] IMPLICIT URCB,
            lcb         [2] IMPLICIT LCB,
            sgcb        [3] IMPLICIT SGCB,
            gocb        [4] IMPLICIT GoCB,
            msvcb       [5] IMPLICIT MSVCB
        }
    },
    moreFollows      [1] IMPLICIT Boolean DEFAULT 1
}

GetAllCBValues-ErrorPDU ::= ServiceError — 8.3.6`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-select', placeholder: '选择逻辑节点（必填）', required: true },
    {
      key: 'acsi',
      label: '控制块类型 acsiClass',
      type: 'select',
      options: ACSI_DEFS.filter((a) => CB_KEYS.includes(a.key)).map((a) => ({ value: a.key, label: `${a.label}（${a.key}）`, color: a.color })),
    },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
  ],
}
