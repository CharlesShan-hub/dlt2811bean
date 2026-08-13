export default {
  title: '读RPC接口定义 rpc-iface-def (8.13.4)',
  desc: '获取RPC接口的详细定义',
  asn1: `GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {
    interface        [0] IMPLICIT VisibleString,
    referenceAfter   [1] IMPLICIT VisibleString OPTIONAL
}

GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE {
    method           [0] IMPLICIT SEQUENCE OF SEQUENCE {
        name          [0] IMPLICIT VisibleString,
        version       [1] IMPLICIT INT32U,
        timeout       [2] IMPLICIT INT32U,
        request       [3] IMPLICIT DataDefinition,
        response      [4] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetRpcInterfaceDefinition-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读远程过程调用接口定义服务用于获取指定接口的所有方法的定义，服务的参数见表79。

**表79 读远程过程调用接口定义服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| interface | | VisibleString |
| referenceAfter[0..1] | | VisibleString |
| **Response+** | | |
| method[1..n] | | |
| name | method | VisibleString |
| version | method | INT32U |
| timeout | method | INT32U |
| request | method | DataDefinition |
| response | method | DataDefinition |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

interface是接口的名称，referenceAfter用于通知服务器返回指定方法之后的结果。name是方法的名称，version是方法的版本，timeout是该方法执行的超时时间，request是请求参数的格式定义，response是响应结果的格式定义。

### 服务要求

远程过程调用接口定义的服务要求如下。

a) version用于客户和服务器间的版本适配。客户应提供向前兼容的能力，服务器提供方法的版本低于客户时，应能正确适配并进行调用。

b) 若客户版本低于服务器版本，应及时对客户程序进行升级，而应不强行发起调用请求。

c) timeout用于说明服务器执行该方法的超时时间。超过timeout定义的时间仍未收到服务器响应的情况下，客户可认为该请求失败。

d) 读远程过程调用接口定义服务应返回指定接口的所有方法的定义。一帧报文无法返回所有方法时，服务器应按顺序返回其中的部分结果，返回的每一个方法应是完整的，同时设置moreFollows参数，通知客户数据未能完全响应。客户应根据响应的结果，修改参数referenceAfter，再次发起新的读远程过程调用接口定义请求。`,
}