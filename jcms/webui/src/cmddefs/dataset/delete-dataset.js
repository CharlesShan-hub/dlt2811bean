// 8.5.4 删除数据集（DeleteDataSet）

export default {
  title: '删除数据集 delete-dataset (8.5.4)',
  desc: '删除动态创建的数据集',
  asn1: `DeleteDataSet-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference
}

DeleteDataSet-ResponsePDU ::= NULL

DeleteDataSet-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

删除数据集服务用于删除指定的数据集，服务的参数见表 38。

**表 38 删除数据集服务参数**

| 服务/参数 | 数据类型 |
|-----------|----------|
| **Request** | |
| datasetReference | ObjectReference |
| **Response+** | |
| **Response-** | |
| serviceError | ServiceError |
`,
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsNewDs' },
  ],
}