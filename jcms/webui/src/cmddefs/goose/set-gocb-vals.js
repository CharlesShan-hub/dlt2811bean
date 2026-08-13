export default {
  title: '设置GOOSE控制块值 set-gocb-vals (8.9.5)',
  desc: '修改GOOSE控制块属性',
  asn1: `SetGoCBValues-RequestPDU ::= SEQUENCE {
    gocb             [0] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        goEna         [1] IMPLICIT BOOLEAN OPTIONAL,
        goID          [2] IMPLICIT VisibleString129 OPTIONAL,
        datSet        [3] IMPLICIT ObjectReference OPTIONAL
    }
}

SetGoCBValues-ResponsePDU ::= NULL

SetGoCBValues-ErrorPDU ::= SEQUENCE {
    result           [0] IMPLICIT SEQUENCE OF ServiceError
} — 8.9.5`,
}