export default {
  title: '读编辑定值组值 get-edit-sg (8.6.5)',
  desc: '读取编辑缓冲区中的定值',
  asn1: `GetEditSGValue-RequestPDU ::= SEQUENCE {
    data                [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        fc                [1] IMPLICIT FunctionalConstraint
    }
}

GetEditSGValue-ResponsePDU ::= SEQUENCE {
    value               [0] IMPLICIT SEQUENCE OF Data,
    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetEditSGValue-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读编辑定值组用于获取编辑定值组的数据，服务的参数见表 44。

**表 44 读编辑定值组值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| data [1..n] | | |
| reference | data | ObjectReference |
| fc | data | FunctionalConstraint |
| **Response+** | | |
| value [1..n] | | Data |
| moreFollows [0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

功能约束 fc 的值为 SG 或 SE。
`,
  params: [
    { key: 'refs', label: '定值引用', type: 'refs-list', required: true, placeholder: 'LD/LN.DO.DA，如 PROT/OCPTOC2.StrVal' },
    { key: 'fc', label: '功能约束 fc', type: 'select', required: false, options: [{ value: 'SG', label: 'SG（定值组）' }, { value: 'SE', label: 'SE（编辑缓冲区）' }] },
  ],
}