export default {
  title: '读文件属性 get-file-attrs (8.12.4)',
  desc: '获取服务器文件的属性信息',
  asn1: `GetFileAttributeValues-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255
}

GetFileAttributeValues-ResponsePDU ::= SEQUENCE {
    fileEntry        [0] IMPLICIT SEQUENCE {
        FileName      [0] IMPLICIT VisibleString129,
        FileSize      [1] IMPLICIT INT32U,
        LastModified  [2] IMPLICIT UtcTime,
        CheckSum      [3] IMPLICIT INT32U
    }
}

GetFileAttributeValues-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

读文件属性值服务的参数见表75。

**表75 读文件属性值服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| fileName | | VisibleString255 |
| **Response+** | | |
| fileEntry | | FileEntry |
| **Response-** | | |
| serviceError | | ServiceError |`,
  params: [
    { key: 'file', label: '文件路径 file', type: 'text', required: true, placeholder: '如 /config/myfile.txt' },
  ],
}