export default {
  title: '设置多播采样值控制块值 set-msvcb-vals (8.10.3)',
  desc: '修改多播采样值控制块属性',
  asn1: `SetMSVCBValues-RequestPDU ::= SEQUENCE {
    msvcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        svEna         [1] IMPLICIT BOOLEAN OPTIONAL,
        msvID         [2] IMPLICIT VisibleString129 OPTIONAL,
        datSet        [3] IMPLICIT ObjectReference OPTIONAL,
        smpMod        [4] IMPLICIT SmpMod OPTIONAL,
        smpRate       [5] IMPLICIT INT16U OPTIONAL,
        optFlds       [6] IMPLICIT MSVOptFlds OPTIONAL
    }
}

SetMSVCBValues-ResponsePDU ::= NULL

SetMSVCBValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF ServiceError
}`,
  doc: `## 协议原文

### 服务参数

设置多播采样值控制块值服务的参数见表64。

**表64 设置多播采样值控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| msvcb[1..n] | | |
| reference | msvcb | ObjectReference |
| svEna[0..1] | msvcb | BOOLEAN |
| msvID[0..1] | msvcb | VisibleString129 |
| datSet[0..1] | msvcb | ObjectReference |
| smpMod[0..1] | msvcb | SmpMod |
| smpRate[0..1] | msvcb | INT16U |
| optFlds[0..1] | msvcb | MSVOptFlds |
| **Response+** | | |
| **Response-** | | |
| result[1..n] | | ServiceError |
`,
}