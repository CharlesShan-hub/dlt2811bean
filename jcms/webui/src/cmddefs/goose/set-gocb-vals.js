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
    result           [0] IMPLICIT SEQUENCE OF ServiceError
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
}