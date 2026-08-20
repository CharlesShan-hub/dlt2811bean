export default {
  title: '读日志控制块值 get-lcb-vals (8.8.2)',
  desc: '获取日志控制块的所有属性',
  asn1: `GetLCBValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetLCBValues-ResponsePDU ::= SEQUENCE {
    lcb              [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        value         [1] IMPLICIT LCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetLCBValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读日志控制块值服务用于获取日志控制块的所有属性，服务的参数见表52。

**表52 读日志控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| lcbReference[1..n] | | ObjectReference |
| **Response+** | | |
| error/lcb[1..n] | | ServiceError/LCB |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| result | | ServiceError |
`,
  params: [
    { key: 'refs', label: 'LCB 引用', type: 'refs-list', required: true, cascade: true, cb: 'lcb', placeholder: 'LD/LN.lcbName，如 LD0/LLN0.lcb1' },
  ],
}