# DL/T 2811 通信协议项目介绍

## 项目概述

这是一个基于 **DL/T 2811** 标准的电力通信协议实现，对标 **IEC 61850 / IEC 61869-9** 标准体系。项目使用 Java 实现，包含完整的通信协议栈、数据模型、PER 编解码、安全加密（国密）和 CLI 工具。

## 技术栈

- **语言**: Java 17+
- **构建工具**: Maven
- **编解码**: PER（Packed Encoding Rules，ASN.1 UPER 变体）
- **传输层**: TCP/IP + TLS（国密 SM2/SM3/SM4）
- **数据帧**: 直接以太网帧发送（GOOSE/SV，基于 Pcap4J + Npcap/libpcap）
- **安全**: 国密算法（SM2 签名/加密、SM3 哈希、SM4 对称加密）
- **工具库**: Lombok、Pcap4J、Bouncy Castle、SLF4J
- **测试**: JUnit 5（含 1100+ 集成测试，基于 Loopback 回环架构）

## 项目结构

```
dlt2811bean/
├── pom.xml                          # Maven 构建配置
├── win_test.ps1                     # Windows 测试脚本
├── docs/
│   ├── standard-2811.md             # DL/T 2811 标准文档（中文）
│   └── project-prompt.md            # 本文件
├── src/main/java/com/ysh/dlt2811bean/
│   ├── config/                      # 配置加载（YAML）
│   │   ├── CmsConfig.java
│   │   ├── CmsConfigLoader.java
│   │   └── CmsConfigInjector.java
│   │
│   ├── per/                         # PER 编解码层
│   │   ├── io/                      # PER 输入/输出流
│   │   │   ├── PerInputStream.java
│   │   │   └── PerOutputStream.java
│   │   ├── types/                   # PER 基础类型编解码
│   │   │   ├── PerBoolean.java
│   │   │   ├── PerInteger.java
│   │   │   ├── PerReal.java
│   │   │   ├── PerEnumerated.java
│   │   │   ├── PerOctetString.java
│   │   │   ├── PerVisibleString.java
│   │   │   ├── PerUtf8String.java
│   │   │   ├── PerBitString.java
│   │   │   ├── PerNull.java
│   │   │   ├── PerChoice.java
│   │   │   └── PerObjectIdentifier.java
│   │   └── exception/
│   │       └── PerDecodeException.java
│   │
│   ├── datatypes/                   # 2811 数据模型
│   │   ├── type/                    # 抽象类型基类
│   │   │   ├── CmsType.java         # 所有类型的基接口
│   │   │   ├── AbstractCmsType.java
│   │   │   ├── AbstractCmsScalar.java
│   │   │   ├── AbstractCmsNumeric.java
│   │   │   ├── AbstractCmsString.java
│   │   │   ├── AbstractCmsEnumerated.java
│   │   │   ├── AbstractCmsCompound.java
│   │   │   ├── AbstractCmsCollection.java
│   │   │   ├── AbstractCmsCodedEnum.java
│   │   │   ├── AbstractCmsPackedList.java
│   │   │   ├── AbstractCmsChoice.java
│   │   │   ├── AbstractCmsDataUnit.java
│   │   │   ├── CmsScalar.java
│   │   │   ├── CmsNumeric.java
│   │   │   ├── CmsCompound.java
│   │   │   ├── CmsCollection.java
│   │   │   ├── CmsCodedEnum.java
│   │   │   ├── CmsPackedList.java
│   │   │   ├── CmsString.java
│   │   │   ├── CmsEnumerated.java
│   │   │   ├── CmsDataUnit.java
│   │   │   └── CmsField.java        # 字段注解
│   │   ├── numeric/                 # 数值类型
│   │   │   ├── CmsBoolean.java
│   │   │   ├── CmsInt8.java / CmsInt16.java / CmsInt32.java / CmsInt64.java
│   │   │   ├── CmsInt8U.java / CmsInt16U.java / CmsInt24U.java / CmsInt32U.java / CmsInt64U.java
│   │   │   ├── CmsFloat32.java / CmsFloat64.java
│   │   ├── string/                  # 字符串类型
│   │   │   ├── CmsVisibleString.java / CmsUtf8String.java / CmsOctetString.java / CmsBitString.java
│   │   │   ├── CmsObjectName.java / CmsObjectReference.java / CmsSubReference.java
│   │   │   ├── CmsEntryID.java / CmsFC.java
│   │   ├── enumerated/              # 枚举类型
│   │   │   ├── CmsServiceError.java / CmsOrCat.java / CmsAddCause.java / CmsSmpMod.java
│   │   ├── code/                    # CODED ENUM 类型
│   │   │   ├── CmsQuality.java / CmsCheck.java / CmsTriggerConditions.java
│   │   │   ├── CmsReasonCode.java / CmsTimeQuality.java / CmsDbpos.java / CmsTcmd.java
│   │   │   ├── CmsRcbOptFlds.java / CmsLcbOptFlds.java / CmsMsvcbOptFlds.java
│   │   ├── compound/                # 复合类型
│   │   │   ├── CmsUtcTime.java / CmsBinaryTime.java / CmsTimeStamp.java
│   │   │   ├── CmsOriginator.java / CmsPhyComAddr.java / CmsEntryTime.java
│   │   │   ├── CmsFileEntry.java / CmsBRCB.java / CmsURCB.java / CmsLCB.java
│   │   │   ├── CmsGoCB.java / CmsMSVCB.java / CmsSGCB.java
│   │   ├── data/                    # Data 类型（CHOICE）
│   │   │   ├── CmsData.java / CmsDataDefinition.java
│   │   ├── collection/              # 集合类型
│   │   │   ├── CmsArray.java / CmsStructure.java
│   │   └── packed/                  # Packed List 类型
│   │       └── CmsPackedListImpl.java
│   │
│   ├── service/                     # 服务层
│   │   ├── protocol/
│   │   │   ├── types/               # 协议报文结构
│   │   │   │   ├── CmsApdu.java     # APDU（应用协议数据单元）
│   │   │   │   ├── CmsApch.java     # APCH（应用协议控制头）
│   │   │   │   └── CmsAsdu.java     # ASDU（应用服务数据单元）
│   │   │   └── enums/
│   │   │       ├── MessageType.java # 报文类型（REQUEST/RESPONSE/INDICATION）
│   │   │       └── ServiceName.java # 服务名称枚举
│   │   ├── svc/                     # 具体服务实现
│   │   │   ├── AsduFactory.java     # ASDU 工厂
│   │   │   ├── association/         # 关联服务（Associate/Release/Abort）
│   │   │   ├── negotiation/         # 协商服务
│   │   │   ├── test/                # 测试服务
│   │   │   ├── directory/           # 目录服务（Server/LD/LN 目录）
│   │   │   ├── data/                # 数据服务（Get/Set DataValues/Directory/Definition）
│   │   │   ├── dataset/             # 数据集服务（Create/Delete/Get/Set）
│   │   │   ├── control/             # 控制服务（Select/Operate/Cancel/CommandTermination/TimeActivatedOperate）
│   │   │   ├── report/              # 报告服务（Report/BRCB/URCB）
│   │   │   ├── setting/             # 定值服务（SelectActiveSG/SelectEditSG/SetEditSGValue/ConfirmEditSGValues/GetEditSGValue/SGCB/LCB/QueryLog）
│   │   │   ├── goose/               # GOOSE 服务（Get/Set GoCBValues + SendGooseMessage）
│   │   │   ├── sv/                  # 采样值服务（Get/Set MSVCBValues + SendMSVMessage）
│   │   │   ├── file/                # 文件服务（Get/Set/Delete File + FileDirectory/Attributes）
│   │   │   └── rpc/                 # RPC 服务（Interface/Method Directory/Definition + RpcCall）
│   │   └── testutil/                # 测试工具
│   │
│   ├── transport/                   # 传输层
│   │   ├── app/                     # 应用入口
│   │   │   ├── CmsServer.java       # 服务器（TCP + TLS + GOOSE 发布）
│   │   │   └── CmsClient.java       # 客户端
│   │   ├── io/                      # IO 层
│   │   │   ├── CmsConnection.java   # 连接管理
│   │   │   ├── CmsClientTransport.java
│   │   │   ├── CmsServerTransport.java
│   │   │   └── CmsTransportListener.java
│   │   ├── session/                 # 会话管理
│   │   │   ├── CmsSession.java / CmsClientSession.java / CmsServerSession.java
│   │   │   ├── KeepAliveManager.java / PendingRequest.java
│   │   │   ├── SessionState.java / AssociationIdGenerator.java
│   │   ├── protocol/                # 协议分发
│   │   │   ├── CmsDispatcher.java   # 请求分发器
│   │   │   ├── CmsServiceHandler.java # 服务处理器接口
│   │   │   ├── CmsFrameManager.java # 帧管理
│   │   │   ├── association/         # 关联处理器
│   │   │   ├── negotiation/         # 协商处理器
│   │   │   ├── test/                # 测试处理器
│   │   │   ├── data/                # 数据处理器
│   │   │   ├── dataset/             # 数据集处理器
│   │   │   ├── control/             # 控制处理器
│   │   │   ├── report/              # 报告处理器
│   │   │   ├── setting/             # 定值处理器
│   │   │   ├── goose/               # GOOSE 处理器
│   │   │   ├── sv/                  # 采样值处理器
│   │   │   ├── file/                # 文件处理器
│   │   │   ├── log/                 # 日志处理器
│   │   │   └── rpc/                 # RPC 处理器
│   │   └── goose/                   # GOOSE 发布引擎（新增）
│   │       ├── GooseConfig.java     # GOOSE 发布配置
│   │       ├── GooseStateMachine.java # stNum/sqNum 状态机
│   │       ├── GooseFrameBuilder.java # 以太网帧构建器
│   │       ├── GooseDataProvider.java # 数据提供接口
│   │       └── GoosePublisher.java  # 发布引擎（基于 Pcap4J）
│   │
│   ├── scl/                         # SCL 配置解析
│   │   ├── SclReader.java / SclDocument.java
│   │   └── model/                   # SCL 数据模型
│   │       ├── SclHeader.java / SclHitem.java
│   │       ├── SclSubstation.java / SclCommunication.java
│   │       ├── SclIED.java / SclDataTypeTemplates.java
│   │
│   ├── security/                    # 国密安全
│   │   ├── GmSslContext.java / GmAuthenticator.java
│   │   ├── GmCertificateParser.java / GmTrustManager.java
│   │   ├── GmSignature.java / GmCredentialManager.java
│   │
│   ├── utils/                       # 工具类
│   │   └── CmsColor.java            # 终端颜色
│   │
│   └── cli/                         # CLI 命令行工具
│       ├── CmsServerCli.java        # 服务器 CLI
│       ├── CmsClientCli.java        # 客户端 CLI
│       └── handler/                 # CLI 命令处理器（每个服务对应一个）
│           ├── CliContext.java / CliSettingHandler.java / HelpHandler.java
│           ├── AssociateHandler / ReleaseHandler / AbortHandler / TestHandler
│           ├── NegotiateHandler / ConnectHandler / ConnectTlsHandler
│           ├── ServerDirHandler / LdDirHandler / LnDirHandler
│           ├── GetDataValuesHandler / SetDataValuesHandler
│           ├── GetAllValuesHandler / GetAllDefHandler / GetAllCbHandler
│           ├── SelectHandler / SelectWithValueHandler / CancelHandler
│           ├── OperateHandler / TimeActOperateHandler
│           ├── FileGetHandler / FileSetHandler / FileDeleteHandler
│           ├── FileDirHandler / FileAttrHandler
│           ├── IfaceDirHandler / IfaceDefHandler / MethodDirHandler / MethodDefHandler / RpcHandler
│           ├── SetMsvcbHandler / MsvcbValHandler
│           ├── StatusHandler / ClearHandler / CloseHandler / ExitHandler
│           └── ...（共约 40+ 个 handler）
│
└── src/test/java/com/ysh/dlt2811bean/
    ├── transport/
    │   ├── app/                     # Loopback 集成测试
    │   │   ├── LoopbackTest.java    # 测试基类（启动服务器+客户端）
    │   │   ├── associate/ / negotiation/ / test/
    │   │   ├── directory/ / data/ / dataset/ / control/
    │   │   ├── report/ / setting/ / log/
    │   │   ├── goose/ / sv/ / file/ / rpc/
    │   │   └── BidirectionalLoopbackTest.java
    │   ├── session/ / protocol/
    │   └── goose/                   # GOOSE 发布引擎测试
    │       ├── GooseStateMachineTest.java
    │       ├── GooseFrameBuilderTest.java
    │       └── GoosePublisherLoopbackTest.java
    ├── scl/ / security/
    ├── service/svc/                 # 服务单元测试
    └── datatypes/ / per/            # 数据类型和 PER 编解码测试
```

## 通信协议架构

### 分层模型

```
CLI (CmsClientCli / CmsServerCli)
        │
        ▼
服务层 (service/svc/)
  ├── 关联 (Associate / Release / Abort)
  ├── 协商 (Negotiate)
  ├── 测试 (Test)
  ├── 目录 (GetServerDirectory / GetLogicalDeviceDirectory / GetLogicalNodeDirectory)
  ├── 数据 (GetDataValues / SetDataValues / GetDataDirectory / GetDataDefinition)
  ├── 数据集 (CreateDataSet / DeleteDataSet / GetDataSetDirectory / GetDataSetValues / SetDataSetValues)
  ├── 控制 (Select / SelectWithValue / Cancel / Operate / CommandTermination / TimeActivatedOperate)
  ├── 报告 (Report / GetBRCBValues / SetBRCBValues / GetURCBValues / SetURCBValues)
  ├── 定值 (SelectActiveSG / SelectEditSG / SetEditSGValue / ConfirmEditSGValues / GetEditSGValue / SGCB / LCB / QueryLog)
  ├── GOOSE (GetGoCBValues / SetGoCBValues / SendGooseMessage)
  ├── 采样值 (GetMSVCBValues / SetMSVCBValues / SendMSVMessage)
  ├── 文件 (GetFile / SetFile / DeleteFile / GetFileDirectory / GetFileAttributeValues)
  └── RPC (GetRpcInterfaceDirectory / GetRpcMethodDirectory / GetRpcInterfaceDefinition / GetRpcMethodDefinition / RpcCall)
        │
        ▼
协议层 (service/protocol/)
  ├── APCH (应用协议控制头)
  │   ├── 协议版本号
  │   ├── 报文类型 (REQUEST / RESPONSE_POSITIVE / RESPONSE_NEGATIVE / INDICATION)
  │   └── 服务码 (关联到具体服务)
  ├── ASDU (应用服务数据单元)
  │   └── 具体服务的参数编码
  └── APDU = APCH + ASDU
        │
        ▼
传输层 (transport/)
  ├── TCP Server (CmsServerTransport) — 处理 ACSI 请求-响应服务
  ├── TLS Server (GmSslContext) — 国密加密传输
  └── GOOSE Publisher (GoosePublisher) — 直接以太网帧发送
        │
        ▼
PER 编解码层 (per/)
  └── ASN.1 UPER 变体编码
```

### 通信流程

```
Client                          Server
  │                                │
  ├── Associate (TCP connect) ────→│
  │                                ├── 验证身份（可选国密）
  │   ←── Associate+ ─────────────│
  │                                │
  ├── GetServerDirectory ─────────→│
  │   ←── 目录列表 ───────────────│
  │                                │
  ├── GetDataValues(ref) ────────→│
  │   ←── 数据值 ────────────────│
  │                                │
  ├── SetGoCBValues(goEna=true) ──→│
  │                                ├── GoosePublisher.start()
  │                                ├── 开始发送 GOOSE 帧（以太网 0x88B8）
  │   ←── Response+ ─────────────│
  │                                │
  ├── Release ────────────────────→│
  │   ←── Release+ ───────────────│
  │                                └── GoosePublisher.stop()
```

## 关键设计决策

1. **PER 编解码**: 使用 ASN.1 UPER 变体，所有数据类型继承自 `CmsType<T>`，实现 `encode(PerOutputStream)` / `decode(PerInputStream)` 方法
2. **服务处理器模式**: 每个服务对应一个 `CmsServiceHandler`，通过 `CmsDispatcher` 根据服务码分发请求
3. **Loopback 测试架构**: 测试时启动一个嵌入式的 Server + Client，通过本地 TCP 回环验证请求-响应流程
4. **GOOSE/SV 发布**: 不经过 TCP/IP 协议栈，直接通过 Pcap4J 发送原始以太网帧（类型 0x88B8 / 0x88BA）
5. **国密安全**: 支持 SM2 证书认证、SM3 哈希校验、SM4 加密传输

## 测试

- **1100+ 集成测试**: 基于 Loopback 回环架构，覆盖所有服务
- **单元测试**: 覆盖所有数据类型、PER 编解码、状态机
- **运行方式**: `mvn test` 或 `powershell -File win_test.ps1`

## 构建与运行

```bash
# 编译
mvn compile

# 运行测试
mvn test

# 启动服务器
mvn exec:java -Dexec.mainClass="com.ysh.dlt2811bean.cli.CmsServerCli"

# 启动客户端
mvn exec:java -Dexec.mainClass="com.ysh.dlt2811bean.cli.CmsClientCli"
```

## 依赖的外部组件

- **Npcap/WinPcap**（Windows）或 **libpcap**（Linux/macOS）— GOOSE/SV 发布时需要
- **Bouncy Castle** — 国密算法支持
- **Pcap4J** — Java 的 Pcap 封装库