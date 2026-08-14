export default {
  title: '设置GOOSE控制块值 set-gocb-vals (8.9.5)',
  desc: '修改GOOSE控制块属性',
  asn1: `SetGoCBValues-RequestPDU ::= SEQUENCE {
    gocb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        goEna         [1] IMPLICIT BOOLEAN OPTIONAL,
        goID          [2] IMPLICIT VisibleString129 OPTIONAL,
        datSet        [3] IMPLICIT ObjectReference OPTIONAL
    }
}

SetGoCBValues-ResponsePDU ::= NULL

SetGoCBValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF SEQUENCE {
        error         [0] IMPLICIT ServiceError OPTIONAL,
        goEna         [1] IMPLICIT ServiceError OPTIONAL,
        goID          [2] IMPLICIT ServiceError OPTIONAL,
        datSet        [3] IMPLICIT ServiceError OPTIONAL
    }
}`,
  doc: `## 协议原文

### 服务参数

设置GOOSE控制块值服务的参数见表61。

**表61 设置GOOSE控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| gocb[1..n] | | |
| reference | gocb | ObjectReference |
| goEna[0..1] | gocb | BOOLEAN |
| goID[0..1] | gocb | VisibleString129 |
| datSet[0..1] | gocb | ObjectReference |
| **Response+** | | |
| **Response-** | | |
| result[1..n] | | ServiceError |
`,
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'ref', label: 'GoCB 引用 ref', type: 'cb-select', cb: 'gocb', required: true, dependsOn: 'ln' },
    { key: 'go-id', label: 'GOOSE ID go-id', type: 'text', required: false, placeholder: 'VisibleString129', inline: 'cfg' },
    { key: 'dat-set', label: '数据集 dat-set', type: 'text', required: false, placeholder: 'LD/LN.dsName', inline: 'cfg' },
    { key: 'go-ena', label: 'GOOSE 使能 go-ena', type: 'switch', required: false },
  ],
}