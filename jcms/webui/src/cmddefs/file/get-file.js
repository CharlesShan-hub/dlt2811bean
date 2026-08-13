export default {
  title: '读取文件 get-file (8.12.1)',
  desc: '从服务器读取文件内容',
  asn1: `GetFile-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255,
    startPosition    [1] IMPLICIT INT32U
}

GetFile-ResponsePDU ::= SEQUENCE {
    fileData         [0] IMPLICIT SEQUENCE OF OCTETSTRING,
    endOfFile        [1] IMPLICIT BOOLEAN OPTIONAL
}

GetFile-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读文件服务用于客户从服务器读取文件，服务的参数见表72。

**表72 读文件服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| fileName | | VisibleString255 |
| startPosition | | INT32U |
| **Response+** | | |
| fileData[0..n] | | OCTETSTRING |
| endOfFile[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

startPosition表示读文件的起始位置，服务器根据startPosition返回从指定位置开始的文件数据，返回的数据长度不超过一个ASDU的最大限制。startPosition从1开始。endOfFile表示是否到达文件尾。

### 服务要求

读文件的服务要求如下。

a) fileName应使用完整路径，以"/"起始。

b) 每一个GetFile请求，服务器只返回一个响应。客户应重复请求不同起始位置的数据，直到文件结束。文件读取结束时，服务器可关闭所读的文件。客户长时间未读后续数据的情况下，服务器应具有超时机制，自动关闭相关文件。

c) startPosition等于0时，表示客户放弃读取后续数据，服务器应关闭所读的文件。`,
  params: [
    { key: 'file', label: '远程文件路径 file', type: 'text', required: true, placeholder: '如 /config/myfile.txt' },
    { key: 'output', label: '本地保存路径 output', type: 'text', required: false, placeholder: '不指定则只打印信息（可选）' },
  ],
}