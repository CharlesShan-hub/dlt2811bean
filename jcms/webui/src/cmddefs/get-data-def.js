// 8.4.4 读数据定义（GetDataDefinition）
import { FC_OPTIONS } from './common.js'

export default {
  title: '读数据定义 get-data-def (8.4.4)',
  desc: '获取一组数据对象或数据属性的结构定义（GetDataDefinition）',
  asn1: `GetDataDefinition-RequestPDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
    }
}

GetDataDefinition-ResponsePDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        cdcType       [0] IMPLICIT VisibleString OPTIONAL,
        definition    [1] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetDataDefinition-ErrorPDU ::= ServiceError — 8.4.4`,
  doc: `## 协议原文

### 服务参数

读数据定义服务用于获取一组数据对象或数据属性的结构定义，服务的参数见表 34。

**表 34 读数据定义服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`data\` [1..n] | | |
| \`reference\` | data | \`ObjectReference\` |
| \`fc\` [0..1] | data | \`FunctionalConstraint\` |
| **Response+** | | |
| \`data\` [1..n] | | |
| \`cdcType\` [0..1] | data | \`VisibleString\` |
| \`definition\` | data | \`DataDefinition\` |
| \`moreFollows\` [0..1] | | \`BOOLEAN\` |
| **Response-** | | |
| \`serviceError\` | | \`ServiceError\` |

### 服务要求

1. \`data\` 是数据对象的情况下，响应时应设置 \`cdcType\` 为对应的 CDC 类型。\`data\` 是数据属性的情况下，响应时应设置 \`cdcType\` 为空。
2. 一帧报文无法返回所有数据的定义时，服务器应按顺序返回其中的部分结果，返回的每一组定义应是完整的，同时设置 \`moreFollows\` 参数，通知客户数据未能完全响应。客户应根据响应的结果，修改参数队列，再次发起新的读数据定义请求。
3. 请求队列中的某一个数据无法访问时，应返回错误原因，并继续处理下一个数据。
4. 数据不包含指定 \`fc\` 的内容时，应返回错误原因。`,
  params: [
    { key: 'refs', label: '数据引用 refs', type: 'refs-list', cascade: true, placeholder: 'LD/LN/DO/DA 逐级选择' },
    { key: 'fc', label: '功能约束 fc', type: 'select', options: FC_OPTIONS },
  ],
}
