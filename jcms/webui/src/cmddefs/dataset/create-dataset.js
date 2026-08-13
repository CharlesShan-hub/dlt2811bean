// 8.5.3 创建数据集（CreateDataSet）

export default {
  title: '创建数据集 create-dataset (8.5.3)',
  desc: '动态创建新的数据集或追加成员',
  asn1: `CreateDataSet-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference,
    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
    memberData          [2] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        fc                [1] IMPLICIT FunctionalConstraint
    }
}

CreateDataSet-ResponsePDU ::= NULL

CreateDataSet-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

创建数据集服务用于动态创建新的数据集，服务的参数见表 37。

**表 37 创建数据集服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| datasetReference | | ObjectReference |
| referenceAfter [0..1] | | ObjectReference |
| memberData [1..n] | | |
| reference | memberData | ObjectReference |
| fc | memberData | FunctionalConstraint |
| **Response+** | | |
| **Response-** | | |
| serviceError | | ServiceError |

### 服务要求

动态创建的数据集应支持持久数据集和非持久数据集两类。非持久性数据集在关联释放后自动删除。持久数据集即使服务器重新启动也应不自动删除。

接收到的请求中未指定 referenceAfter 时，应创建一个新的数据集。接收到的请求中指定了 referenceAfter 时，应在现有数据集之后增加新的成员，referenceAfter 为现有数据集的最后一个成员。预定义的数据集或已关联报告控制块的数据集应不允许增加新成员。数据集成员为 FCD 或 FDCA。
`,
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsNewDs' },
    { key: 'members', label: '成员列表', type: 'text', required: true, placeholder: '如 "PROT/GGIO1.ST.Ind1,ST PROT/GGIO1.MX.AnIn1,MX"' },
    { key: 'after', label: '分页游标 after', type: 'text', required: false, placeholder: '（可选）从该引用之后继续创建' },
  ],
}