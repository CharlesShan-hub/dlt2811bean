export default {
  title: '读GOOSE参考 get-go-ref (8.9.2)',
  desc: '读取GOOSE参考信息',
  asn1: `GetGoReference-RequestPDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    memberOffset     [1] IMPLICIT SEQUENCE OF INT16U
}

GetGoReference-ResponsePDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    confRev          [1] IMPLICIT INT32U,
    datSet           [2] IMPLICIT ObjectReference,
    memberData       [3] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint
    }
}

GetGoReference-ErrorPDU ::= ServiceError — 8.9.2`,
}