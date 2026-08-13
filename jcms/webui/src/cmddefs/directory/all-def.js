// 8.3.5 读所有数据定义（GetAllDataDefinition）
import { FC_OPTIONS } from '../common.js'

export default {
  title: '读所有数据定义 all-def (8.3.5)',
  desc: '获取指定逻辑设备或逻辑节点下所有数据对象的定义（GetAllDataDefinition）',
  asn1: `GetAllDataDefinition-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT CHOICE {
        ldName         [0] IMPLICIT ObjectName,
        lnReference    [1] IMPLICIT ObjectReference
    },
    fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
}

GetAllDataDefinition-ResponsePDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT SubReference,
        cdcType       [1] IMPLICIT VisibleString OPTIONAL,
        definition    [2] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetAllDataDefinition-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读所有数据定义服务用于获取指定逻辑设备或逻辑节点下所有数据对象的定义，服务的参数见表 29。

**表 29 读所有数据定义服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`ldName/lnReference\` | | \`ObjectName/ObjectReference\` |
| \`fc\` [0..1] | | \`FunctionalConstraint\` |
| \`referenceAfter\` [0..1] | | \`ObjectReference\` |
| **Response+** | | |
| \`data\` [0..n] | | |
| \`reference\` | data | \`SubReference\` |
| \`cdcType\` [0..1] | data | \`VisibleString\` |
| \`definition\` | data | \`DataDefinition\` |
| \`moreFollows\` [0..1] | | \`BOOLEAN\` |
| **Response-** | | |
| \`serviceError\` | | \`ServiceError\` |

参数 \`fc\` 用于筛选特定功能约束的数据属性，其定义见表 28。参数 \`data\` 的 \`reference\` 是相对于 \`ldName\` 或 \`lnReference\` 的子引用名，参数 \`cdcType\` 是数据对象的 CDC 类型。

### 服务要求

1. 数据不包含指定 \`fc\` 的内容时，返回的结果中应不包含该数据。
2. 参数 \`fc\` 为 \`XX\` 或空时，应返回指定逻辑设备或逻辑节点内全部数据属性的定义（不包括功能约束 \`SE\`）。
3. 仅当参数 \`fc\` 明确指定为 \`SE\` 时，服务器返回功能约束 \`SE\` 的数据属性定义。功能约束 \`SE\` 的数据属性定义应与功能约束 \`SG\` 完全相同。`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-cascade', placeholder: 'LD → LN 逐级选择（必填）', required: true },
    { key: 'fc', label: '功能约束 fc', type: 'select', options: FC_OPTIONS },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
    { key: 'auto-pull', label: '自动续拉 auto-pull', type: 'auto-pull-switch' },
  ],
}
