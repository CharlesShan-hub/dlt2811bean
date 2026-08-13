export default {
  title: '读RPC方法定义 rpc-method-def (8.13.5)',
  desc: '获取RPC方法的详细定义',
  asn1: `GetRpcMethodDefinition-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF VisibleString
}

GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {
    method           [0] IMPLICIT SEQUENCE OF SEQUENCE {
        timeout       [0] IMPLICIT INT32U,
        version       [1] IMPLICIT INT32U,
        request       [2] IMPLICIT DataDefinition,
        response      [3] IMPLICIT DataDefinition
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetRpcMethodDefinition-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读远程过程调用方法定义服务用于获取一组方法的定义，服务的参数见表80。

**表80 读远程过程调用方法定义服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| reference[1..n] | | VisibleString |
| **Response+** | | |
| error/method[1..n] | | |
| timeout | error/method | INT32U |
| version | error/method | INT32U |
| request | error/method | DataDefinition |
| response | error/method | DataDefinition |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

reference是方法的引用名，格式见8.13.1。timeout、version、request、response的含义见8.13.4。

### 服务要求

读远程过程调用方法定义的服务要求如下。

a) 一帧报文无法返回所有方法的定义时，服务器应按顺序返回其中的部分结果，返回的每一组定义应是完整的，同时设置moreFollows参数，通知客户数据未能完全响应。客户应根据响应的结果，修改参数队列，再次发起新的读远程过程调用方法定义请求。

b) 请求队列中的某一个方法无法访问时，应返回错误原因，并继续处理下一个方法。`,
}