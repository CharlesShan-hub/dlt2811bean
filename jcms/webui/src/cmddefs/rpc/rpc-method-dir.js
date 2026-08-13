export default {
  title: '读RPC方法目录 rpc-method-dir (8.13.3)',
  desc: '获取RPC接口下方法目录列表',
  asn1: `GetRpcMethodDirectory-RequestPDU ::= SEQUENCE {
    interface        [0] IMPLICIT VisibleString OPTIONAL,
    referenceAfter   [1] IMPLICIT VisibleString OPTIONAL
}

GetRpcMethodDirectory-ResponsePDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF VisibleString,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetRpcMethodDirectory-ErrorPDU ::= ServiceError — 8.13.3`,
}