// 8.5.2 设置数据集值（SetDataSetValues）

export default {
  title: '设置数据集值 set-dataset-values (8.5.2)',
  desc: '批量设置数据集成员的值',
  asn1: `SetDataSetValues-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference,
    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
    memberValue         [2] IMPLICIT SEQUENCE OF Data
}

SetDataSetValues-ResponsePDU ::= NULL

SetDataSetValues-ErrorPDU ::= SEQUENCE {
    result              [0] IMPLICIT SEQUENCE OF ServiceError
}`,
  doc: `## 协议原文

### 服务参数

设置数据集值服务用于批量设置数据集成员的值，服务的参数见表 36。

**表 36 设置数据集值服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| datasetReference | ObjectReference |
| referenceAfter [0..1] | ObjectReference |
| memberValue [1..n] | Data |
| **Response+** | |
| **Response-** | |
| result [1..n] | ServiceError |

### 服务要求

设置数据集值服务的要求如下。

a) 服务的每一个数据应按数据集内的索引顺序排列。

b) 未指定 referenceAfter 的情况下，应从数据集的第一个成员开始设置数据值。

c) 指定了 referenceAfter 的情况下，应从 referenceAfter 成员之后按顺序设置数据值。

d) 所有数据集值设置成功时返回 Response+，部分或全部失败时返回 Response-。在 Response-中，依次返回每个数据集值的设置结果。
`,
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsRelayEna' },
    { key: 'values', label: '成员值列表', type: 'text', required: true, placeholder: '空格分隔，如 "10 20 30"' },
    { key: 'after', label: '分页游标 after', type: 'text', required: false, placeholder: '（可选）从该引用之后继续设置' },
  ],
}