export default {
  title: '读多播采样值控制块值 get-msvcb-vals (8.10.2)',
  desc: '读取多播采样值控制块属性值',
  asn1: `GetMSVCBValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetMSVCBValues-ResponsePDU ::= SEQUENCE {
    errorMsvcb       [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        msvcb         [1] IMPLICIT MSVCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetMSVCBValues-ErrorPDU ::= ServiceError — 8.10.2`,
}