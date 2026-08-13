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

RpcCall-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

远程过程调用服务用于请求服务器执行指定的方法，服务的参数见表81。

**表81 远程过程调用服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| method | | VISIBLESTRING |
| reqData/callID | | Data/OCTETSTRING |
| **Response+** | | |
| rspData | | Data |
| nextCallID[0..1] | | OCTETSTRING |
| **Response-** | | |
| serviceError | | ServiceError |

method是调用方法的引用，格式见8.13.1；reqData是调用方法的请求参数；rspData是调用后的返回结果。

### 服务要求

远程过程调用的服务要求如下。

a) 一帧报文无法返回所有结果时，服务器应按顺序返回其中的部分结果，返回的每一组结果应是完整的，同时设置nextCallID参数，通知客户数据未能完全响应。

b) nextCallID是一组十六进制串，其含义由服务器定义，服务器应能根据这一组十六进制串直接定位到上一次调用的位置并继续执行。客户识别出响应结果中含有nextCallID时，应再次发起新的调用请求，参数method应与前一次调用相同，参数callID设置为前一次响应返回的nextCallID。服务器继续执行未完成的调用，直至调用全部完成。`,
}