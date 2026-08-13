export default {
  title: '读GOOSE元素 get-goose-elem (8.9.3)',
  desc: '读取GOOSE数据集元素信息',
  asn1: `GetGOOSEElementNumber-RequestPDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    memberData       [1] IMPLICIT SEQUENCE OF SEQUENCE {
        reference     [0] IMPLICIT ObjectReference,
        fc            [1] IMPLICIT FunctionalConstraint
    }
}

GetGOOSEElementNumber-ResponsePDU ::= SEQUENCE {
    gocbReference    [0] IMPLICIT ObjectReference,
    confRev          [1] IMPLICIT INT32U,
    datSet           [2] IMPLICIT ObjectReference,
    memberOffset     [3] IMPLICIT SEQUENCE OF INT16U
}

GetGOOSEElementNumber-ErrorPDU ::= ServiceError — 8.9.3`,
}