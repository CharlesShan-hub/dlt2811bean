# dlt2811bean

**DL/T 2811-2024 CMS 协议 Java 实现** — 新国标变电站二次系统通信

## 项目结构

```
dlt2811bean/
├── pom.xml                          # 单模块 Maven 项目
└── src/main/java/com/ysh/dlt2811bean/
    ├── core/                        # 核心基础层
    │   ├── per/                     #   ASN.1 PER 编解码引擎
    │   ├── model/                   #   IEC 61850 信息模型
    │   │   ├── datatype/            #     BDA 基本数据属性
    │   │   └── rcb/                 #     报告控制块
    │   └── time/                    #   时间处理
    ├── scl/                         # SCL 解析器（ICD/CID/SCD/IID）
    │   └── internal/                #   SCL XML 内部模型
    ├── platform/                    # 平台适配层（TCP + 以太网帧）
    ├── cms/                         # CMS 协议栈（替代 MMS）
    │   ├── transport/               #   传输子层
    │   ├── pdu/                     #   PDU 编解码（PER）
    │   └── service/                 #   服务语义层（读/写/报告/控制）
    ├── goose/                       # GOOSE 协议栈（二层以太网）
    │   ├── publisher/               #   发布端（心跳+重传）
    │   └── subscriber/              #   订阅端（状态机+超时检测）
    ├── sv/                          # SV 采样值协议栈
    │   ├── codec/                   #   帧格式编解码（9-2LE / 9-1）
    │   ├── publisher/               #   发送端
    │   └── subscriber/              #   接收端
    ├── security/                    # 安全模块（TLS + 国密 + 证书）
    └── tools/                       # 工具集（测试+抓包+示例）
```

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+

### 编译

```bash
mvn clean install -DskipTests
```

### 运行测试

```bash
mvn test
```

## 技术依赖

| 库 | 用途 |
|---|------|
| Bouncy Castle (bcprov/bctls) | SM2/SM3/SM4 国密算法 |
| pcap4j | GOOSE/SV 原始以太网帧收发 |
| SLF4J + Logback | 日志框架 |

## License

见 [LICENSE](LICENSE) 文件。
