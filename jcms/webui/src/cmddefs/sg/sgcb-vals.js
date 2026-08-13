export default {
  title: '读定值组控制块值 sgcb-vals (8.6.6)',
  desc: '读取定值组控制块的值',
  asn1: `GetSGCBValues-RequestPDU ::= SEQUENCE {
    sgcbReference       [0] IMPLICIT SEQUENCE OF ObjectReference
} — 8.6.6

GetSGCBValues-ResponsePDU ::= SEQUENCE {
    errorOrSgcb         [0] IMPLICIT SEQUENCE OF CHOICE {
        error             [0] IMPLICIT ServiceError,
        sgcb              [1] IMPLICIT SGCB
    }
} — 8.6.6

GetSGCBValues-ErrorPDU ::= ServiceError — 8.6.6`,
}