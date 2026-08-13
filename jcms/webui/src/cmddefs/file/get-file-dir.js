export default {
  title: '读文件目录 get-file-dir (8.12.5)',
  desc: '获取服务器文件目录列表',
  asn1: `GetFileDirectory-RequestPDU ::= SEQUENCE {
    pathName         [0] IMPLICIT VisibleString255 OPTIONAL,
    startTime        [1] IMPLICIT TimeStamp OPTIONAL,
    stopTime         [2] IMPLICIT TimeStamp OPTIONAL,
    fileAfter        [3] IMPLICIT VisibleString255 OPTIONAL
}

GetFileDirectory-ResponsePDU ::= SEQUENCE {
    fileEntry        [0] IMPLICIT SEQUENCE OF SEQUENCE {
        FileName      [0] IMPLICIT VisibleString129,
        FileSize      [1] IMPLICIT INT32U,
        LastModified  [2] IMPLICIT UtcTime,
        CheckSum      [3] IMPLICIT INT32U
    },
    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE
}

GetFileDirectory-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

列文件目录的参数见表76。

**表76 列文件目录服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| pathName[0..1] | | VisibleString255 |
| startTime[0..1] | | TimeStamp |
| stopTime[0..1] | | TimeStamp |
| fileAfter[0..1] | | VisibleString255 |
| **Response+** | | |
| fileEntry[0..n] | | FileEntry |
| moreFollows[0..1] | | BOOLEAN |
| **Response-** | | |
| serviceError | | ServiceError |

### 服务要求

列文件目录的服务要求如下：

a) startTime和stopTime表示文件目录的起始和截止时间，返回结果应在起始和截止时间之间，包含起始和截止时间；

b) pathName应采用完整路径名，格式为"/××××××"。

> 注：pathName中不要求支持"*""?"等通配符，具体实现时，厂商可自主决定。`,
  params: [
    { key: 'path', label: '路径筛选 path', type: 'text', required: false, placeholder: '如 /config（可选）' },
    { key: 'after', label: '起始条目 after', type: 'text', required: false, placeholder: '从该文件名之后开始（可选）' },
  ],
}