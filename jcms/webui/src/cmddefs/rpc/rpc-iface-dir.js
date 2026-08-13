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

GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读远程过程调用接口目录服务用于获取服务器所有可用的调用接口，服务的参数见表77。

**表77 读远程过程调用接口目录服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| referenceAfter[0..1] | | VisibleString |
| **Response+** | | |
| reference[0..n] | | VisibleString |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |`,
}