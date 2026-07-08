# scl2 纯模型层开发计划

## 目标

构建 `scl2` 包——SCL (IEC 61850-6) 配置文件的纯 Java POJO 模型层。**零业务依赖**，只做 XML ↔ POJO 的双向映射。

## 原则

- 纯 POJO，无业务方法（无 `resolveDataValue`、`setDataValue` 等）
- 无外部依赖（不依赖 jcms 协议栈）
- 只提供导航方法（`addXxx`、`findXxx`）
- 集合使用 `final List<> = new ArrayList<>()` 内联初始化
- 逐步完善，每步可编译、可验证

---

## 第一阶段：模型填充（耗时最长）

> 目标：对照真实 SCD 文件和 IEC 61850-6 标准，逐模块完善字段。

### Step 1.1 Header 模块
- [ ] `SclHeader` — 确认所有字段正确（id, version, revision, toolId, nameStructure, text）
- [ ] `SclHitem` — 确认字段（version, revision, when, who, what, why）
- **验证**: 用 `sample-scd-full.scd` 的 `<Header>` 测试解析

### Step 1.2 Substation 模块 ← **首要补齐**
- [ ] `SclSubstation` — name, desc
- [ ] `SclVoltageLevel` — name, desc
- [ ] `SclVoltage` — value, multiplier, unit（已建，但需确认 SCL schema 定义）
- [ ] `SclBay` — name, desc
- [ ] `SclConductingEquipment` — name, desc, type
- [ ] `SclPowerTransformer` — name, desc, type **NEW**
- [ ] `SclTransformerWinding` — name, desc, type **NEW**
- [ ] `SclTerminal` — 所有属性 **NEW**
- [ ] `SclConnectivityNode` — name, pathName **NEW**
- [ ] `SclSubEquipment` — name, phase **NEW**
- **验证**: 对照 `sample-scd-full.scd` Substation 节逐元素核对

### Step 1.3 Communication 模块
- [ ] `SclSubNetwork` — name, desc, type, text, bitRate, bitRateUnit
- [ ] `SclConnectedAP` — iedName, apName, PhysConn 支持
- [ ] `SclPhysConn` — type + P 子元素列表 **NEW**
- [ ] `SclGSE` / `SclSMV` / `SclAddress` — 已有，确认字段完整
- **验证**: 对照 SCD Communication 节

### Step 1.4 IED 模块
- [ ] `SclIED` — name, desc, services
- [ ] `SclAccessPoint` — name, server
- [ ] `SclServer` — LDevice 列表
- [ ] `SclLDevice` — inst, desc, LN 列表
- [ ] `SclLNBase / SclLN` — 所有控制块集合 + DOI/DataSet/Input
- [ ] `SclServices` — 服务能力字段（参考 SCD 和标准确认全部字段）
- **验证**: 对照 SCD IED 节

### Step 1.5 Instance + Input + Control 模块
- [ ] `SclDOI / SclSDI / SclDAI` — 已有，确认字段完整
- [ ] `SclDataSet / SclFCDA / SclExtRef` — 已有，确认字段完整
- [ ] `SclGSEControl / SclLogControl / SclReportControl / SclSampledValueControl` — 已有
- [ ] `SclReportControl` 的 `TrgOps`、`OptFields`、`RptEnabled` 子元素是否需要独立类？
- **验证**: 对照 SCD LN 下的实例和控制块

### Step 1.6 Template 模块
- [ ] `SclDataTypeTemplates` — 索引查询已实现
- [ ] `SclLNodeType / SclDOType / SclDAType / SclEnumType` — 已有
- [ ] `SclDO / SclSDO / SclDA / SclBDA / SclEnumVal` — 已有
- [ ] 确认 `SclDA` 的 `dchg`、`qchg`、`dupd` 触发条件属性是否需要
- **验证**: 对照 SCD DataTypeTemplates 节

---

## 第二阶段：SclReader 解析器

> 目标：实现 StAX 流式解析，将 XML 逐节解析为 SclDocument。

### Step 2.1 解析框架
- [ ] `parseDocument()` 主循环，识别 5 个顶层标签
- [ ] `getAttr()` / `boolAttr()` / `intAttr()` 辅助方法

### Step 2.2 逐节解析
- [ ] `parseHeader()` — `<Header>` + `<Hitem>`
- [ ] `parseSubstation()` — 树形递归解析 Substation
- [ ] `parseCommunication()` — SubNetwork → ConnectedAP → GSE/SMV/Address
- [ ] `parseIed()` — IED → AccessPoint → Server → LDevice → LN
- [ ] `parseLn()` — LN 下的 DOI/DataSet/控制块/Input
- [ ] `parseDataTypeTemplates()` — LNodeType/DOType/DAType/EnumType

### Step 2.3 集成测试
- [ ] 用 `sample-scd-full.scd` 完整解析
- [ ] 与 SclQuery 集成验证查询功能
- [ ] 对比老 scl 包的解析结果是否一致

---

## 第三阶段：Ref 引用层完善

> 目标：统一的引用解析系统。

### Step 3.1 SclRefParser
- [ ] 实现 `parse()` 正则解析逻辑
- [ ] 单元测试：各种引用格式

### Step 3.2 SclQuery 查询门面
- [ ] 补全 `IedQuery` 查询方法
- [ ] 补全 `DataTypeQuery` 查询方法
- [ ] 补充 Communication 和 Substation 查询

---

## 第四阶段：老 scl 包重构

> 目标：老 scl 包依赖 scl2，只保留业务逻辑。

### Step 4.1 适配
- [ ] 老 scl 的 `SclServer` 删除 `resolveDataValue` 等业务方法 → 移到 util
- [ ] 老 scl 的 `SclLN` 删除 `collectDataValues` 等 → 移到 lnBuilder
- [ ] `SclRef` 和 `RefParts` 统一为 scl2 的 `SclRef`
- [ ] 修复 `SclRefValidator.resolveDoType()` 为 null 的 bug

### Step 4.2 清理
- [ ] 删除空壳类 `SclLNHelper`、`SclLNDataResolver`
- [ ] 重命名 `SclSetSetDataValueResolver` → `SclSetDataValueResolver`
- [ ] 统一命名规范

---

## 模块优先级

```
P0 ┤ Header + Template（简单，已有基础）
P0 ┤ IED + Instance + Input + Control（核心，已有基础）
P1 ┤ Substation 补齐（缺 PowerTransformer/Terminal 等新类）
P1 ┤ Communication 补齐（缺 PhysConn/Text/BitRate）
P1 ┤ SclReader 解析器
P2 ┤ Ref 引用层完善
P2 ┤ SclQuery 查询门面
P3 ┤ 老 scl 包重构适配
```

---

## 验证方法

每完成一个模块，对照 `sample-scd-full.scd` 做以下检查：

1. **字段覆盖** — SCD 文件中的所有属性是否都有对应字段？
2. **字段命名** — 是否与 SCL 标准属性名一致？
3. **类型正确** — 数值型用 Integer/Boolean，不是全 String
4. **层级正确** — 父子关系是否匹配 XML 树结构？
