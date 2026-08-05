// 8.4.1 读数据值（GetDataValues）

export default {
  title: '读数据值 get-data-values (8.4.1)',
  desc: '获取一组数据对象或数据属性的值（GetDataValues）',
  asn1: `GetDataValues-RequestPDU ::= SEQUENCE {
    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
    }
}

GetDataValues-ResponsePDU ::= SEQUENCE {
    value            [0] IMPLICIT SEQUENCE OF Data,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetDataValues-ErrorPDU ::= ServiceError — 8.4.1`,
  doc: `## 协议原文

### 服务参数

读数据值服务用于获取一组数据对象或数据属性的值，服务的参数见表 31。

**表 31 读数据值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`data\` [1..n] | | |
| \`reference\` | data | \`ObjectReference\` |
| \`fc\` [0..1] | data | \`FunctionalConstraint\` |
| **Response+** | | |
| \`value\` [1..n] | | \`Data\` |
| \`moreFollows\` [0..1] | | \`BOOLEAN\` |
| **Response-** | | |
| \`serviceError\` | | \`ServiceError\` |

参数 \`fc\` 用于指定功能约束条件，筛选特定类别的数据属性。\`fc\` 为 \`XX\` 或空时，不进行筛选。

### 服务要求

1. 一帧报文无法返回所有数据的值时，服务器应按顺序返回其中的部分结果，返回的每一个 \`value\` 应是完整的，同时设置 \`moreFollows\` 参数，通知客户数据未能完全响应。客户应根据响应的结果，修改参数队列，再次发起新的读数据值请求。
2. 请求队列中的某一个数据无法访问时，应返回错误原因，并继续处理下一个数据。
3. 数据不包含指定 \`fc\` 的内容时，应返回错误原因。`,
  params: [
    { key: 'refs', label: '数据引用', type: 'refs-list', cascade: true },
  ],
}