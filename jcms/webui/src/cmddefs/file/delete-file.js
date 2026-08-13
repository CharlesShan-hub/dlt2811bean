export default {
  title: '删除文件 delete-file (8.12.3)',
  desc: '删除服务器上的文件',
  asn1: `DeleteFile-RequestPDU ::= SEQUENCE {
    fileName         [0] IMPLICIT VisibleString255
}

DeleteFile-ResponsePDU ::= NULL

DeleteFile-ErrorPDU ::= ServiceError`,
  doc: `## 协议原文

### 服务参数

删除文件服务的参数见表74。

**表74 删除文件服务参数**

| 服务/参数 | 所属 | 数据类型 |
|-----------|------|----------|
| **Request** | | |
| fileName | | VisibleString255 |
| **Response+** | | |
| **Response-** | | |
| serviceError | | ServiceError |`,
  params: [
    { key: 'file', label: '文件路径 file', type: 'text', required: true, placeholder: '如 /config/myfile.txt' },
  ],
}