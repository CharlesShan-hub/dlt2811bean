export default {
  title: '读GOOSE参考 get-go-ref (8.9.2)',
  desc: '读取GOOSE参考信息',
  asn1: `GetGoReference-RequestPDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    memberOffset     [1] IMPLICIT SEQUENCE OF INT16U
}

GetGoReference-ResponsePDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    confRev          [1] IMPLICIT INT32U,
    datSet           [2] IMPLICIT ObjectReference,
    memberData       [3] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint
    }
}

GetGoReference-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读GOOSE引用服务的参数见表58。

**表58 读GOOSE引用服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| gocbReference | | ObjectReference |
| memberOffset[1..n] | | INT16U |
| **Response+** | | |
| gocbReference | | ObjectReference |
| confRev | | INT32U |
| datSet | | ObjectReference |
| memberData[1..n] | | |
| reference | memberData | ObjectReference |
| fc | memberData | FunctionalConstraint |
| **Response-** | | |
| serviceError | | ServiceError |
`,
}