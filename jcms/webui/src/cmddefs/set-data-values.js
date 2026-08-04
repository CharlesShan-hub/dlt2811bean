// 8.4.2 设置数据值（SetDataValues）
import { FC_OPTIONS } from './common.js'

export default {
  title: '设置数据值 set-data-values (8.4.2)',
  desc: '批量设置一组数据的值（SetDataValues）',
  asn1: `SetDataValues-RequestPDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,
        value         [2] IMPLICIT Data
    }
}

SetDataValues-ResponsePDU ::= NULL

SetDataValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF ServiceError
} — 8.4.2`,
  doc: `## 协议原文

### 服务参数

设置数据值服务用于批量设置一组数据的值，服务的参数见表 32。

**表 32 设置数据值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`data\` [1..n] | | |
| \`reference\` | data | \`ObjectReference\` |
| \`fc\` [0..1] | data | \`FunctionalConstraint\` |
| \`value\` | data | \`Data\` |
| **Response+** | | |
| **Response-** | | |
| \`result\` [1..n] | | \`ServiceError\` |

### 服务要求

设置数据值服务的每一个数据由 \`Reference\` 唯一索引，当包含 \`fc\`（功能约束）时，表示数据值为 FCD 的值；不包含 \`fc\`（功能约束）时，表示数据值为所有数据属性的值。所有数据值设置成功时返回 Response+，部分或全部失败时返回 Response-。在 Response- 中，依次返回每个数据值的设置结果。`,
  params: [
    { key: 'pairs', label: '数据值对 pairs', type: 'text', placeholder: 'LD/LN.DO.DA=value，多个用空格分隔' },
    { key: 'fc', label: '功能约束 fc', type: 'select', options: FC_OPTIONS },
  ],
}
