export default {
  title: '设置编辑定值组值 set-edit-sg (8.6.3)',
  desc: '设置编辑缓冲区中的定值',
  asn1: `SetEditSGValue-RequestPDU ::= SEQUENCE {
    data                [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference         [0] IMPLICIT ObjectReference,
        value             [1] IMPLICIT Data
    }
} — 8.6.3

SetEditSGValue-ResponsePDU ::= NULL — 8.6.3

SetEditSGValue-ErrorPDU ::= SEQUENCE {
    result              [0] IMPLICIT SEQUENCE OF ServiceError
} — 8.6.3`,
}