// 8.5.1 读数据集值（GetDataSetValues）

export default {
  title: '读数据集值 get-dataset-values (8.5.1)',
  desc: '获取数据集所有成员的值',
  asn1: `GetDataSetValues-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference,
    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
}

GetDataSetValues-ResponsePDU ::= SEQUENCE {
    value               [0] IMPLICIT SEQUENCE OF Data,
    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetDataSetValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 8.5.1.1 服务参数

读数据集值服务用于批量获取数据集成员的值，服务的参数见表 35。

**表 35 读数据集值服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| datasetReference | ObjectReference |
| referenceAfter [0..1] | ObjectReference |
| **Response+** | |
| memberValue [1..n] | Data |
| moreFollows [0..1] | BOOLEAN |
| **Response-** | |
| serviceError | ServiceError |

### 8.5.1.2 服务要求

a) 未指定 referenceAfter 时，应从数据集的第一个成员开始按顺序返回数据值。指定了 referenceAfter 时，应从 referenceAfter 成员之后按顺序返回数据集中的数据值。

b) 一个 ASDU 无法返回所有数据值时，应设置 moreFollows 为 TRUE。数据集不存在或数据集中的某一个数据无法访问时，应返回错误响应。`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'ds', label: '数据集名称', type: 'dataset-select', required: true, dependsOn: 'ln' },
    { key: 'after', label: '分页游标 after', type: 'ds-member-after', required: false, placeholder: '（可选）从该成员之后继续返回' },
    { key: 'auto-pull', label: '自动续拉分页 auto-pull', type: 'auto-pull-switch', required: false },
  ],
}