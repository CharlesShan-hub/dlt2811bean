// 8.3.4 读所有数据值（GetAllDataValues）
import { FC_OPTIONS } from './common.js'

export default {
  title: '读所有数据值 all-data (8.3.4)',
  desc: '获取指定逻辑设备或逻辑节点下所有数据对象的值（GetAllDataValues）',
  asn1: `GetAllDataValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT CHOICE {
        ldName         [0] IMPLICIT ObjectName,
        lnReference    [1] IMPLICIT ObjectReference
    },
    fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,
    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL
}

GetAllDataValues-ResponsePDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT SubReference,
        value         [1] IMPLICIT Data
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetAllDataValues-ErrorPDU ::= ServiceError — 8.3.4`,
  doc: `## 协议原文

### 服务参数

读所有数据值服务用于获取指定逻辑设备或逻辑节点下所有数据对象的值，服务的参数见表 27。

**表 27 读所有数据值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`ldName/lnReference\` | | \`ObjectName/ObjectReference\` |
| \`fc\` [0..1] | | \`FunctionalConstraint\` |
| \`referenceAfter\` [0..1] | | \`ObjectReference\` |
| **Response+** | | |
| \`data\` [0..n] | | |
| \`reference\` | data | \`SubReference\` |
| \`value\` | data | \`Data\` |
| \`moreFollows\` [0..1] | | \`BOOLEAN\` |
| **Response-** | | |
| \`serviceError\` | | \`ServiceError\` |

### 服务要求

1. 数据不包含指定 \`fc\` 的内容时，返回的结果中应不包含该数据。
2. 参数 \`fc\` 为 \`XX\` 或空时，应返回指定逻辑设备或逻辑节点内全部数据属性的值（不包括功能约束 \`SE\`）。仅当参数 \`fc\` 明确指定为 \`SE\` 时，服务器才返回功能约束 \`SE\` 的数据属性值。仅当选择编辑定值组服务后，功能约束 \`SE\` 的数据属性值有效。`,
  params: [
    { key: 'ln', label: '逻辑设备/节点 ln', type: 'ln-cascade', placeholder: 'LD → LN 逐级选择（必填）', required: true },
    { key: 'fc', label: '功能约束 fc', type: 'select', options: FC_OPTIONS },
    { key: 'after', label: '起始引用 after', type: 'ln-ref-select', placeholder: '可选：该 LN 下的引用' },
    { key: 'auto-pull', label: '自动续拉 auto-pull', type: 'auto-pull-switch' },
  ],
}
