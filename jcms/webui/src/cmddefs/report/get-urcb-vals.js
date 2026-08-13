export default {
  title: '读非缓存报告控制块值 get-urcb-vals (8.7.4)',
  desc: '读取非缓存报告控制块的属性值',
  asn1: `GetURCBValues-RequestPDU ::= SEQUENCE {
    urcbReference    [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetURCBValues-ResponsePDU ::= SEQUENCE {
    error            [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        urcb          [1] IMPLICIT URCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetURCBValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读非缓存报告控制块值服务用于获取非缓存报告控制块的所有属性，服务的参数见表 49。

**表 49 读非缓存报告控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| urcbReference [1..n] | | ObjectReference |
| **Response+** | | |
| error/urcb [1..n] | | ServiceError/URCB |
| moreFollows [0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

### 服务要求

读非缓存报告控制块值的服务要求如下。

a) 一帧报文无法返回所有非缓存报告控制块的值时，服务器应按顺序返回其中的部分结果，返回的每一个控制块的值应是完整的，同时设置 moreFollows 参数，通知客户数据未能完全响应。客户应根据响应的结果，修改参数队列，再次发起新的请求。

b) 请求队列中的某一个控制块无法访问时，应返回错误原因，并继续处理下一个控制块。
`,
}