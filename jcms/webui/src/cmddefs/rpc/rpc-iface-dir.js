export default {
  title: '读RPC接口目录 rpc-iface-dir (8.13.2)',
  desc: '获取RPC接口目录列表',
  asn1: `GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {
    referenceAfter   [0] IMPLICIT VisibleString OPTIONAL
}

GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF VisibleString,
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError — 8.13.2`,
}