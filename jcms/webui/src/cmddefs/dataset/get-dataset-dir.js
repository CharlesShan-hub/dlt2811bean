// 8.5.5 读数据集目录（GetDataSetDirectory）

export default {
  title: '读数据集目录 get-dataset-dir (8.5.5)',
  desc: '获取数据集所有成员的引用名和功能约束',
  asn1: `GetDataSetDirectory-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference,
    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
}

GetDataSetDirectory-ResponsePDU ::= SEQUENCE {
    memberData          [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        fc                [1] IMPLICIT FunctionalConstraint
    },
    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetDataSetDirectory-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 8.5.5.1 服务参数

读数据集目录服务用于批量获取数据集成员的引用名，服务的参数见表 39。

**表 39 读数据集目录服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| datasetReference | | ObjectReference |
| referenceAfter [0..1] | | ObjectReference |
| **Response+** | | |
| memberData [1..n] | | |
| reference | memberData | ObjectReference |
| fc | memberData | FunctionalConstraint |
| moreFollows [0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

### 8.5.5.2 服务要求

接收到的请求中未指定 referenceAfter 时，应从第一个成员开始读数据集目录。接收到的请求中指定了 referenceAfter 时，应从数据集的指定成员之后读数据集目录。`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'ds', label: '数据集名称', type: 'dataset-select', required: true, dependsOn: 'ln' },
    { key: 'after', label: '分页游标 after', type: 'text', required: false, placeholder: '（可选）从该引用之后继续返回' },
    { key: 'auto-pull', label: '自动续拉分页 auto-pull', type: 'auto-pull-switch', required: false },
  ],
}