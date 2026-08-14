export default {
  title: '读GOOSE控制块值 get-gocb-vals (8.9.4)',
  desc: '读取GOOSE控制块属性值',
  asn1: `GetGoCbValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetGoCbValues-ResponsePDU ::= SEQUENCE {
    gocb             [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        value         [1] IMPLICIT GoCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetGoCbValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读GOOSE控制块值服务的参数见表60。

**表60 读GOOSE控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| gocbReference[1..n] | | ObjectReference |
| **Response+** | | |
| error/gocb[1..n] | | ServiceError/GoCB |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |
`,
  params: [
    { key: 'refs', label: 'GoCB 引用', type: 'refs-list', required: true, placeholder: 'LD/LN.gocbName，如 LD0/LLN0.gocb1' },
  ],
}