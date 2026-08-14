export default {
  title: '读定值组控制块值 sgcb-vals (8.6.6)',
  desc: '读取定值组控制块的值',
  asn1: `GetSGCBValues-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetSGCBValues-ResponsePDU ::= SEQUENCE {
    sgscb               [0] IMPLICIT SEQUENCE OF CHOICE {
        error             [0] IMPLICIT ServiceError,
        value             [1] IMPLICIT SGCB
    },
    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetSGCBValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读定值组控制块值服务用于获取定值组控制块的所有属性，服务的参数见表 45。

**表 45 读定值组控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| sgcbReference [1..n] | | ObjectReference |
| **Response+** | | |
| error/sgcb [1..n] | | ServiceError/SGCB |
| **Response-** | | |
| serviceError | | ServiceError |
`,
  params: [
    { key: 'refs', label: 'SGCB 引用', type: 'refs-list', cascade: true },
  ],
}