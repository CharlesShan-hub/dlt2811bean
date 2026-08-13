export default {
  title: '读编辑定值组值 get-edit-sg (8.6.5)',
  desc: '读取编辑缓冲区中的定值',
  asn1: `GetEditSGValue-RequestPDU ::= SEQUENCE {
    data                [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        fc                [1] IMPLICIT FunctionalConstraint
    }
} — 8.6.5

GetEditSGValue-ResponsePDU ::= SEQUENCE {
    value               [0] IMPLICIT SEQUENCE OF Data,
    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE
} — 8.6.5

GetEditSGValue-ErrorPDU ::= ServiceError — 8.6.5`,
}