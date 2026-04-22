# dlt2811bean

**DL/T 2811-2024 CMS 协议 Java 实现** — 新国标变电站二次系统通信

---

## 数据对象映射

| DL/T 2811位置 |          解释           |         类名         | 标记 |
| :-----------: | :---------------------: | :------------------: | :--: |
|     7.1.1     |     基础类型：布尔      |      CMSBoolean      | base |
|     7.1.2     |     基础类型：整数      |       CmsInt8        | base |
|     7.1.2     |     基础类型：整数      |       CmsInt16       | base |
|     7.1.2     |     基础类型：整数      |       CmsInt32       | base |
|     7.1.2     |     基础类型：整数      |       CmsInt64       | base |
|     7.1.3     |     基础类型：整数      |       CmsInt8U       | base |
|     7.1.3     |     基础类型：整数      |      CmsInt16U       | base |
|     7.1.3     |     基础类型：整数      |      CmsInt32U       | base |
|     7.1.3     |     基础类型：整数      |      CmsInt64U       | base |
|     7.1.4     |    基础类型：浮点数     |      CmsFloat32      | base |
|     7.1.4     |    基础类型：浮点数     |      CmsFloat64      | base |
|     7.1.5     |    基础类型：字符串     |    CmsOctetString    | base |
|     7.1.5     |    基础类型：字符串     |   CmsVisibleString   | base |
|     7.1.5     |    基础类型：字符串     |    CmsUtf8String     | base |
|     7.1.5     |    基础类型：字符串     |     CmsBitString     | base |
|     7.1.6     |     基础类型：枚举      |    CmsEnumerated     | base |
|     7.1.7     |   基础类型：编码枚举    |     CmsCodedEnum     | base |
|     7.1.8     |   基础类型：压缩列表    |    CmsPackedList     | base |
|     7.2.1     |  扩展类型：协调世界时   |      CmsUtcTime      | bean |
|     7.2.1     | （内部字段：时间质量）  |    CmsTimeQuality    | bean |
|     7.2.2     |  扩展类型：二进制时间   |    CmsBinaryTime     | bean |
|     7.3.1     |    公共 ACSI：对象名    |    CmsObjectName     | bean |
|     7.3.2     |   公共 ACSI：对象引用   |  CmsObjectReference  | bean |
|     7.3.3     |    公共 ACSI：子引用    |   CmsSubReference    | bean |
|     7.3.4     |  公共 ACSI：二进制时间  |     CmsTimeStamp     | copy |
|     7.3.5     |   公共 ACSI：双点位置   |       CmsDbpos       | enum |
|     7.3.6     |     公共 ACSI：品质     |      CmsQuality      | code |
|     7.3.7     |   公共 ACSI：挡位命令   |       CmsTcmd        | enum |
|     7.3.8     |   公共 ACSI：条目标识   |      CmsEntryID      | bean |
|     7.3.9     |   公共 ACSI：条目时间   |     CmsEntryTime     | copy |
|    7.3.10     |   公共 ACSI：文件条目   |     CmsFileEntry     | bean |
|    7.3.11     |   公共 ACSI：服务错误   |   CmsServiceError    | enum |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|     7.6.2     |    控制块：触发条件     | CmsTriggerConditions |      |
|     7.6.3     |    控制块：触发原因     |    CmsReasonCode     |      |
|     7.6.4     |  缓存报告控制块 选项域  |    CmsRcbOptFlds     |      |
|     7.6.5     |    日志控制块 选项域    |    CmsLcbOptFlds     |      |
|     7.6.6     | 多播采样值控制块 选项域 |   CmsMsvcbOptFlds    |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |
|               |                         |                      |      |

