export default {
  title: 'RPC调用 rpc-call (8.13.6)',
  desc: '调用远程过程',
  asn1: `RpcCall-RequestPDU ::= SEQUENCE {
    method           [0] IMPLICIT VisibleString,
    reqDataCallID    [1] IMPLICIT CHOICE {
        reqData       [0] IMPLICIT Data,
        callID        [1] IMPLICIT OCTETSTRING
    }
}

RpcCall-ResponsePDU ::= SEQUENCE {
    rspData          [0] IMPLICIT Data,
    nextCallID       [1] IMPLICIT OCTETSTRING OPTIONAL
}

RpcCall-ErrorPDU ::= ServiceError — 8.13.6`,
}