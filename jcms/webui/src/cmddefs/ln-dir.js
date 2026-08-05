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
  doc: `## 协议原文

### 服务参数

读逻辑节点目录服务用于获取逻辑节点内的所有数据对象或控制块，服务的参数见表 25。

**表 25 读逻辑节点目录服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| \`ldName/lnReference\` | \`ObjectName/ObjectReference\` |
| \`acsiClass\` | \`ACSIClass\` |
| \`referenceAfter\` [0..1] | \`ObjectReference\` |
| **Response+** | |
| \`reference\` [0..n] | \`SubReference\` |
| \`moreFollows\` [0..1] | \`BOOLEAN\` |
| **Response-** | |
| \`serviceError\` | \`ServiceError\` |

\`acsiClass\` 参数用于限定请求的对象类型，其定义见表 26。

**表 26 ACSIClass 值**

| ACSIClass | 值 | 含义 |
|-----------|-----|------|
| \`reserved\` | 0 | 保留 |
| \`DataObject\` | 1 | 数据对象 |
| \`DATA-SET\` | 2 | 数据集 |
| \`BRCB\` | 3 | 缓存报告控制块 |
| \`URCB\` | 4 | 非缓存报告控制块 |
| \`LCB\` | 5 | 日志控制块 |
| \`LOG\` | 6 | 日志 |
| \`SGCB\` | 7 | 定值组控制块 |
| \`GoCB\` | 8 | GOOSE 控制块 |
| \`MSVCB\` | 10 | 多播采样值控制块 |

### 服务要求

1. \`acsiClass\` 为 \`DataObject\` 时，请求逻辑节点下所有数据对象及其子数据对象的引用名，引用名应按模型定义的顺序排序。如 \`LD/LN.DO1\`, \`LD/LN.DO1.SDO1\`, \`LD/LN.DO1.SDO2\`。`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-cascade', placeholder: '先选 LD，再选 LN（可选，仅 LD 则查询全部 LN）', required: true },
    {
      key: 'acsi',
      label: '对象类型 acsiClass',
      type: 'select',
      options: ACSI_DEFS.map((a) => ({ value: a.key, label: `${a.label}（${a.key}）`, color: a.color })),
    },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
  ],
}
