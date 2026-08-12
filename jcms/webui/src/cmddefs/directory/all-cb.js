// 8.3.6 读所有控制块值（GetAllCBValues），acsi 下拉与目录树圆点颜色一致
import { ACSI_DEFS } from '../../acsiDefs.js'

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
  doc: `## 协议原文

### 服务参数

读所有控制块值服务用于获取指定逻辑设备或逻辑节点下所有控制块的值，服务的参数见表 30。

**表 30 读所有控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`ldName/lnReference\` | | \`ObjectName/ObjectReference\` |
| \`acsiClass\` | | \`ACSIClass\` |
| \`referenceAfter\` [0..1] | | \`ObjectReference\` |
| **Response+** | | |
| \`cbValue\` [0..n] | | |
| \`reference\` | cbValue | \`SubReference\` |
| \`value\` | cbValue | \`BRCB/URCB/LCB/SGCB/GoCB/MSVCB\` |
| \`moreFollows\` [0..1] | | \`BOOLEAN\` |
| **Response-** | | |
| \`serviceError\` | | \`ServiceError\` |

控制块类型由 \`acsiClass\` 指定，如缓存报告控制块、非缓存报告控制块、定值控制块等。控制块定义见 8.6~8.10。`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-cascade', placeholder: 'LD → LN 逐级选择（必填）', required: true },
    {
      key: 'acsi',
      label: '控制块类型 acsiClass',
      type: 'select',
      options: ACSI_DEFS.filter((a) => CB_KEYS.includes(a.key)).map((a) => ({ value: a.key, label: `${a.label}（${a.key}）`, color: a.color })),
    },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
    { key: 'auto-pull', label: '自动续拉 auto-pull', type: 'auto-pull-switch' },
  ],
}
