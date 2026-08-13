// 8.5.2 设置数据集值（SetDataSetValues）

export default {
  title: '设置数据集值 set-dataset-values (8.5.2)',
  desc: '批量设置数据集成员的值',
  asn1: `SetDataSetValues-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference,
    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,
    memberValue         [2] IMPLICIT SEQUENCE OF Data
} — 8.5.2

SetDataSetValues-ResponsePDU ::= NULL — 8.5.2

SetDataSetValues-ErrorPDU ::= SEQUENCE {
    result              [0] IMPLICIT SEQUENCE OF ServiceError
} — 8.5.2`,
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsRelayEna' },
    { key: 'values', label: '成员值列表', type: 'text', required: true, placeholder: '空格分隔，如 "10 20 30"' },
    { key: 'after', label: '分页游标 after', type: 'text', required: false, placeholder: '（可选）从该引用之后继续设置' },
  ],
}