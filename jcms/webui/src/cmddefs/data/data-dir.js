// 8.4.3 读数据目录（GetDataDirectory）
export default {
  title: '数据目录 data-dir (8.4.3)',
  desc: '获取指定数据对象的子数据对象和数据属性（GetDataDirectory）',
  asn1: `GetDataDirectory-RequestPDU ::= SEQUENCE {
    dataReference    [0] IMPLICIT ObjectReference,
    referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL
}

GetDataDirectory-ResponsePDU ::= SEQUENCE {
    dataAttribute    [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT SubReference,
        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetDataDirectory-ErrorPDU ::= ServiceError — 8.4.3`,
  doc: `## 协议原文

### 服务参数

读数据目录服务用于获取指定数据对象的所有子数据对象和数据属性的引用名，服务的参数见表 33。

**表 33 读数据目录服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| \`dataReference\` | | \`ObjectReference\` |
| \`referenceAfter\` [0..1] | | \`ObjectReference\` |
| **Response+** | | |
| \`dataAttribute\` [0..n] | | |
| \`reference\` | dataAttribute | \`SubReference\` |
| \`fc\` [0..1] | dataAttribute | \`FunctionalConstraint\` |
| \`moreFollows\` [0..1] | | \`BOOLEAN\` |
| **Response-** | | |
| \`serviceError\` | | \`ServiceError\` |

### 服务要求

1. 读数据目录服务的子数据对象应不包含 \`fc\`，数据属性应包含 \`fc\`。
2. 嵌套结构的数据属性，应按深度优先的顺序逐层返回数据属性引用。
3. SCL 定义的 DA 对象含有 \`fc\` 定义，因此结果中应包含 \`fc\`；DO 对象和 BDA 对象不含有 \`fc\` 定义，因此结果中应不包含 \`fc\`。`,
  params: [
    { key: 'ref', label: '数据引用', type: 'refs-list', cascade: true, single: true },
    { key: 'auto-pull', label: '自动续拉 auto-pull', type: 'auto-pull-switch' },
  ],
}
