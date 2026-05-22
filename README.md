# dlt2811bean

**DL/T 2811-2024 CMS 协议 Java 实现** — 新国标变电站二次系统通信

---

## 如何启动

### 加密相关

本项目支持 TLS/国密 SSL 加密传输，基于 BouncyCastle 实现。

需要运行 PowerShell 脚本自动生成测试证书，生成的文件位于 `src/main/resources/certs/`：

```powershell
.\Generate-Test-Certs.ps1
```

如需生成真正的国密证书，需安装 [GmSSL](https://www.gmssl.org/) 工具，然后修改脚本使用国密算法：

```bash
# 生成 SM2 私钥
gmssl genpkey -algorithm sm2 -out ca.key

# 生成自签名证书
gmssl req -new -x509 -key ca.key -out ca.cer

# 转换为 PKCS12
openssl pkcs12 -export -in ca.cer -inkey ca.key -out ca.pfx
```

## GOOSE / SV 支持

GOOSE 和 SV 模块依赖原始以太网帧收发，需要安装 pcap 库。

Windows 下载安装 [Npcap 1.88](https://npcap.com/dist/npcap-1.88.exe)，安装时勾选 "Install in WinPcap API-compatible Mode"。

macOS / Linux：需要给 BPF 设备添加权限：

```bash
sudo chmod 666 /dev/bpf*
```

## 必要的配置

1. 去application.yaml里边的scd目录列表最前面添加真实scd文件目录
2. 测试使用 win_test.ps1
3. 启动服务器cli：win_server.ps1
4. 启动客户端cli：win_client.ps1

注意需要修改一下上边ps1命令里边的java路径。

5. 具体的客户端使用的命令：[Demo: 快速开始](docs/impl-demo.md)

---

### 交互模式案例📖 其他文档导航

| 文档 | 说明 |
|------|------|
| [给ai的快速了解的提示词](docs/project-prompt.md) | 项目背景、架构设计、整体思路 |
| [第七章实现（数据对象映射）](docs/impl-datatypes.md) | 各章节数据类型定义与实现对照 |
| [第六、八章实现（传输层）](docs/standard-per.md) | 传输协议与帧格式详解 |
| [支持功能：PER编码实现](docs/impl-per.md) | PER 编码/解码实现细节 |
| [支持功能：SCL模型解析](docs/impl-scl.md) | SCL 配置文件的读入与模型构建 |
| [支持功能：安全通信](docs/impl-security.md) | TLS / 国密 SSL 实现 |
| [GOOSE 实现](docs/impl-goose.md) | GOOSE 快速报文实现 |
| [标准原文摘录](docs/standard-2811.md) | DL/T 2811 标准相关章节原文 |
| [Demo: 快速开始](docs/impl-demo.md) | cli客户端示例 |
| [Demo: 详细示例](docs/impl-demo2.md) | 直接在命令行使用客户端命令（需要cli版本的客户端服务器已经开启） |
