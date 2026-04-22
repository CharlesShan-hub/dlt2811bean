# dlt2811bean

**DL/T 2811-2024 CMS 协议 Java 实现** — 新国标变电站二次系统通信

---

## 数据对象映射

| 章节 | 类型 | 类名 | 标记 | decode | test |
| :--: | :--: | :--: | :--: | :----: | :--: |
| 7.1.1基础类型 | 布尔 | CMSBoolean | base | ✅ | ✅ |
| 7.1.2基础类型 | 整数 | CmsInt8 | base | ✅ | ✅ |
| 7.1.2基础类型 | 整数 | CmsInt16 | base | ✅ | ✅ |
| 7.1.2基础类型 | 整数 | CmsInt32 | base | ✅ | ✅ |
| 7.1.2基础类型 | 整数 | CmsInt64 | base | ✅ | ✅ |
| 7.1.3基础类型 | 整数 | CmsInt8U | base | ✅ | ✅ |
| 7.1.3基础类型 | 整数 | CmsInt16U | base | ✅ | ✅ |
| 7.1.3基础类型 | 整数 | CmsInt32U | base | ✅ | ✅ |
| 7.1.3基础类型 | 整数 | CmsInt64U | base | ✅ | ✅ |
| 7.1.4基础类型 | 浮点数 | CmsFloat32 | base | ✅ | ⬜ |
| 7.1.4基础类型 | 浮点数 | CmsFloat64 | base | ✅ | ⬜ |
| 7.1.5基础类型 | 字符串 | CmsOctetString | base | ⬜ | ⬜ |
| 7.1.5基础类型 | 字符串 | CmsVisibleString | base | ⬜ | ⬜ |
| 7.1.5基础类型 | 字符串 | CmsUtf8String | base | ⬜ | ⬜ |
| 7.1.5基础类型 | 字符串 | CmsBitString | base | ⬜ | ⬜ |
| 7.1.6基础类型 | 枚举 | CmsEnumerated | base | ⬜ | ⬜ |
| 7.1.7基础类型 | 编码枚举 | CmsCodedEnum | base | ⬜ | ⬜ |
| 7.1.8基础类型 | 压缩列表 | CmsPackedList | base | ⬜ | ⬜ |
| 7.2.1扩展类型 | 协调世界时 | CmsUtcTime | bean | ⬜ | ⬜ |
| 7.2.1扩展类型 | 时间质量 | CmsTimeQuality | bean | ⬜ | ⬜ |
| 7.2.2扩展类型 | 二进制时间 | CmsBinaryTime | bean | ⬜ | ⬜ |
| 7.3.1公共ACSI | 对象名 | CmsObjectName | bean | ⬜ | ⬜ |
| 7.3.2公共ACSI | 对象引用 | CmsObjectReference | bean | ⬜ | ⬜ |
| 7.3.3公共ACSI | 子引用 | CmsSubReference | bean | ⬜ | ⬜ |
| 7.3.4公共ACSI | 二进制时间 | CmsTimeStamp | copy | ⬜ | ⬜ |
| 7.3.5公共ACSI | 双点位置 | CmsDbpos | enum | ⬜ | ⬜ |
| 7.3.6公共ACSI | 品质 | CmsQuality | code | ⬜ | ⬜ |
| 7.3.7公共ACSI | 挡位命令 | CmsTcmd | enum | ⬜ | ⬜ |
| 7.3.8公共ACSI | 条目标识 | CmsEntryID | bean | ⬜ | ⬜ |
| 7.3.9公共ACSI | 条目时间 | CmsEntryTime | copy | ⬜ | ⬜ |
| 7.3.10公共ACSI | 文件条目 | CmsFileEntry | bean | ⬜ | ⬜ |
| 7.3.11公共ACSI | 服务错误 | CmsServiceError | enum | ⬜ | ⬜ |
| 7.3.12公共ACSI | 物理通信地址 | CmsPhyComAddr | bean | ⬜ | ⬜ |
| 7.4功能约束 | 功能约束 | CmsFC | bean | ⬜ | ⬜ |
| 7.5.1控制对象属性 | 控制对象属性 | - | - | - | - |
| 7.5.2控制操作 | 发出者 | CmsOriginator | enum | ⬜ | ⬜ |
| 7.5.3控制操作 | 检测 | CmsCheck | Code | ⬜ | ⬜ |
| 7.5.4控制操作 | 附加原因 | CmsAddCause | enum | ⬜ | ⬜ |
| 7.6.1控制块属性 | 控制块属性 | - | - | - | - |
| 7.6.2控制块 | 触发条件 | CmsTriggerConditions | Code | ⬜ | ⬜ |
| 7.6.3控制块 | 触发原因 | CmsReasonCode | Code | ⬜ | ⬜ |
| 7.6.4控制块 | 缓存报告控制块选项域 | CmsRcbOptFlds | Code | ⬜ | ⬜ |
| 7.6.5控制块 | 日志控制块选项域 | CmsLcbOptFlds | Code | ⬜ | ⬜ |
| 7.6.6控制块 | 多播采样值控制块选项域 | CmsMsvcbOptFlds | Code | ⬜ | ⬜ |
|     7.7.1     |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||
|               |                         |                      |      |||



