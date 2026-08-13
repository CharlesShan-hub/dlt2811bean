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
  params: [
    { key: 'ln', label: '逻辑节点', type: 'ln-cascade', required: true },
    { key: 'ref', label: 'MSVCB 引用 ref', type: 'cb-select', cb: 'msvcb', required: true, dependsOn: 'ln' },
    { key: 'msv-id', label: 'MSV 标识 msv-id', type: 'text', required: false, placeholder: 'VisibleString129', inline: 'cfg' },
    { key: 'dat-set', label: '数据集 dat-set', type: 'text', required: false, placeholder: 'LD/LN.dsName', inline: 'cfg' },
    { key: 'smp-mod', label: '采样模式 smp-mod', type: 'number', required: false, placeholder: '0/1/2', inline: 'smp' },
    { key: 'smp-rate', label: '采样率 smp-rate', type: 'number', required: false, placeholder: 'INT16U', inline: 'smp' },
    { key: 'opt-flds', label: '选项字段 opt-flds', type: 'number', required: false, placeholder: 'MSVCBOptFlds 位掩码' },
    { key: 'sv-ena', label: '采样值使能 sv-ena', type: 'switch', required: false },
  ],
}