export default {
  title: '设置文件 set-file (8.12.2)',
  desc: '向服务器写入文件内容',
  asn1: `SetFile-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255,
    startPosition    [1] IMPLICIT INT32U,
    fileData         [2] IMPLICIT SEQUENCE OF OCTETSTRING,
    endOfFile        [3] IMPLICIT BOOLEAN OPTIONAL
}

SetFile-ResponsePDU ::= NULL

SetFile-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

写文件服务用于客户向服务器写入文件，服务的参数见表73。

**表73 写文件服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| fileName | | VisibleString255 |
| startPosition | | INT32U |
| fileData[0..n] | | OCTETSTRING |
| endOfFile[0..1] | | BOOLEAN |
| **Response+** | | |
| **Response-** | | |
| serviceError | | ServiceError |

fileName和startPosition的定义与GetFile服务相同。endOfFile表示是否到达文件尾。

### 服务要求

写文件的服务要求如下。

a) 根据写入文件的长度，客户应发起多个SetFile请求。第一个请求的startPosition为1，后续每个请求的startPosition和fileData应是连续的。

b) startPosition等于0时，表示客户放弃写入后续数据，服务器应关闭并删除未完成的文件。

c) 文件写入结束时，服务器应保存所写的文件。客户长时间未写入后续数据的情况下，服务器应具有超时机制，自动关闭并删除不完整的文件。`,
  params: [
    { key: 'local', label: '本地文件路径 local', type: 'text', required: true, placeholder: '如 D:/data/myfile.txt' },
    { key: 'remote', label: '远程目标路径 remote', type: 'text', required: true, placeholder: '如 /config/myfile.txt' },
  ],
}