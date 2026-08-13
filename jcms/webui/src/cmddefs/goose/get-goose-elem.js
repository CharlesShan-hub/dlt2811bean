export default {
  title: '读GOOSE元素 get-goose-elem (8.9.3)',
  desc: '读取GOOSE数据集元素信息',
  asn1: `GetGOOSEElementNumber-RequestPDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    memberData       [1] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint
    }
}

GetGOOSEElementNumber-ResponsePDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    confRev          [1] IMPLICIT INT32U,
    datSet           [2] IMPLICIT ObjectReference,
    memberOffset     [3] IMPLICIT SEQUENCE OF INT16U
}

GetGOOSEElementNumber-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读GOOSE元素序号服务的参数见表59。

**表59 读GOOSE元素序号服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| gocbReference | | ObjectReference |
| memberData[1..n] | | |
| reference | memberData | ObjectReference |
| fc | memberData | FunctionalConstraint |
| **Response+** | | |
| gocbReference | | ObjectReference |
| confRev | | INT32U |
| datSet | | ObjectReference |
| memberOffset[1..n] | | INT16U |
| **Response-** | | |
| serviceError | | ServiceError |
`,
}