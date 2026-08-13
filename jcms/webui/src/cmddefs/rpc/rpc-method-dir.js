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

GetRpcMethodDirectory-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读远程过程调用方法目录服务用于获取指定调用接口的所有方法的名称，服务的参数见表78。

**表78 读远程过程调用方法目录服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| interface[0..1] | | VisibleString |
| referenceAfter[0..1] | | VisibleString |
| **Response+** | | |
| reference[0..n] | | VisibleString |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

参数interface是指定的接口名称，参数referenceAfter用于通知服务器只返回referenceAfter之后的方法。肯定响应中的reference是所有方法的名称，moreFollows表示是否返回了全部方法。

### 服务要求

读远程过程调用方法目录的服务要求如下：

a) 没有指定interface时，表示需要获取所有调用接口的所有方法的名称，参数referenceAfter和reference应使用完整的引用名，格式见8.13.1；

b) DataDefinition、Data仅用于描述实例化后的数据，所以应不使用OPTIONAL和Default语法，请求和响应参数均应是明确定义的结构。`,
}