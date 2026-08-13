export default {
  title: '读多播采样值控制块值 get-msvcb-vals (8.10.2)',
  desc: '读取多播采样值控制块属性值',
  asn1: `GetMSVCBValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetMSVCBValues-ResponsePDU ::= SEQUENCE {
    errorMsvcb       [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        msvcb         [1] IMPLICIT MSVCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetMSVCBValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读多播采样值控制块值服务的参数见表63。

**表63 读多播采样值控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| reference[1..n] | | ObjectReference |
| **Response+** | | |
| error/msvcb[1..n] | | ServiceError/MSVCB |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |
`,
  params: [
    { key: 'refs', label: 'MSVCB 引用', type: 'refs-list', required: true, placeholder: 'LD/SV.msvcbName，如 LD0/SV1.msvcb01' },
  ],
}