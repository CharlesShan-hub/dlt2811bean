// 8.5.5 读数据集目录（GetDataSetDirectory）

export default {
  title: '读数据集目录 get-dataset-dir (8.5.5)',
  desc: '获取数据集所有成员的引用',
  asn1: `GetDataSetDirectory-RequestPDU ::= SEQUENCE {
    datasetReference    [0] IMPLICIT ObjectReference,
    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL
} — 8.5.5

GetDataSetDirectory-ResponsePDU ::= SEQUENCE {
    memberData          [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        fc                [1] IMPLICIT FunctionalConstraint
    },
    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
} — 8.5.5

GetDataSetDirectory-ErrorPDU ::= ServiceError — 8.5.5`,
  params: [
    { key: 'ds', label: '数据集引用', type: 'text', required: true, placeholder: 'LD/LN.dsName，如 PROT/LLN0.dsRelayEna' },
    { key: 'after', label: '分页游标 after', type: 'text', required: false, placeholder: '（可选）从该引用之后继续返回' },
  ],
}