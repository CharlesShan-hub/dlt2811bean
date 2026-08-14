export default {
  title: '读缓存报告控制块值 get-brcb-vals (8.7.2)',
  desc: '读取缓存报告控制块的属性值',
  asn1: `GetBRCBValues-RequestPDU ::= SEQUENCE {
    reference        [0] IMPLICIT SEQUENCE OF ObjectReference
}

GetBRCBValues-ResponsePDU ::= SEQUENCE {
    brcb             [0] IMPLICIT SEQUENCE OF CHOICE {
        error         [0] IMPLICIT ServiceError,
        value         [1] IMPLICIT BRCB
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetBRCBValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读缓存报告控制块值服务用于获取缓存报告控制块的所有属性，服务的参数见表 47。

**表 47 读缓存报告控制块值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| brcbReference [1..n] | | ObjectReference |
| **Response+** | | |
| error/brcb [1..n] | | ServiceError/BRCB |
| moreFollows [0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

### 服务要求

读缓存报告控制块值服务的要求如下。

a) 一帧报文无法返回所有缓存报告控制块的值时，服务器应按顺序返回其中的部分结果，返回的每一个控制块的值应是完整的，同时设置 moreFollows 参数，通知客户数据未能完全响应。客户应根据响应的结果，修改参数队列，再次发起新的请求。

b) 请求队列中的某一个控制块无法访问时，应返回错误原因，并继续处理下一个控制块。
`,
  params: [
    { key: 'refs', label: 'BRCB 引用', type: 'refs-list', required: true, placeholder: 'LD/LN.brcbName，如 LD0/LLN0.brcbAlarm' },
  ],
}