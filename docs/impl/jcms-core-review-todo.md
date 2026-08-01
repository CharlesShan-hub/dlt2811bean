# jcms-core 代码审查改进清单

> 审查日期：2026-08-01
> 范围：`jcms-core`（含 `jcms-data` 中被 jcms-core 直接依赖的 `InnerBase`/`V`）
> 方式：人工审查 + 2 个子代理交叉验证（共识打分，排除误报）
> 约定：每项修完跑 `mvn test -pl jcms-core -Dtest="*Data*"` 或全量 `mvn test -pl jcms-core` 验证，勾选完成。

## 优先级

| 级别 | 说明 |
|------|------|
| 🔴 高 | 影响正确性/线上路由，优先修复 |
| 🟠 中 | 性能/数据一致性，按序修复 |
| 🟡 低 | 规范/简洁/工具类，最后清理 |

---

## 🔴 高优先级

### [x] 1. CmsServiceInfo 服务码冲突（BY_CODE 静默覆盖）— ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/info/CmsServiceInfo.java`、`jcms/jcms-utils/src/main/java/com/ysh/jcms/utils/transport/ServiceName.java`
- **依据**：`assets/standard-2811.md` 表 1 服务码（权威）
- **问题**：`SET_DATA_VALUES`(0x31) vs `GET_BRCB_VALUES`(0x31)、`GET_DATA_DIRECTORY`(0x32) vs `SET_BRCB_VALUES`(0x32)、`GET_DATA_DEFINITION`(0x33) vs `GET_URCB_VALUES`(0x33)。静态块 `BY_CODE.put` 按声明顺序后覆盖先。
- **修复**：
  1. `CmsServiceInfo` 全部服务码对照表 1 修正（associate 改为 2=Abort/3=Release、negotiate→0x9A、dataset/sg/report/log/goose/msv/control/file/rpc/test 全部对齐）；
  2. `ServiceName`（线网实际组帧）同步对照表 1 修正（msv、control、file、rpc、test、negotiate）；
  3. 4 个未确认服务（SendGOOSEMessage/GetGoReference/GetGOOSEElementNumber/SendMSVMessage）标准表 1 未分配码，统一 0x00 占位（与 ServiceName 原行为一致）；
  4. 静态块加固：重复服务码（0x00 除外）抛 `IllegalStateException` 防回归。
- **验证**：`mvn test -pl jcms-core` 全量通过 ✅

### [ ] 2. InnerBase.toJson 将纯 hex 字符串大写化（编码值失真）

- **文件**：`jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerBase.java`
- **位置**：L237-L245（String 分支）
- **问题**：非空且全由 hex 字符组成的字符串（如 `"abc123"`、`"cafe"`）被 `toUpperCase()`，encode 路径（`MAPPER.writeValueAsString(InnerBase.toJson(_v))`）会改写用户设置的值。
- **方案**：String 分支原样返回；仅对 `byte[]` 做 hex 规范化。若 equals 规范化仍需要统一大小写，仅在 equals/hashCode 专用路径处理。
- **验证**：对 `CmsData().alt_visible_string("abc123")` 做 encode→decode 往返，值保持 `abc123`；新增回归测试。

## 🟠 中优先级

### [ ] 3. InnerBase.equals/hashCode 每次完整 JSON 序列化

- **文件**：`jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerBase.java`
- **位置**：L72-L82；下游 `CmsType.equals/hashCode`（jcms-core `data/core/CmsType.java` L85-L95）委托 inner
- **问题**：每次比较/哈希都全树 `toJson(toJsonValue())` 重建临时对象，测试断言与 contains/Map key 开销大。
- **方案**：惰性缓存规范化 JSON 串（`_v` 可变，在 syncToInner/decode/rebind 后置 null 失效）；`hashCode` 缓存 int 而非每次序列化。
- **验证**：现有 300+ 测试全绿，性能可感知提升（可加微基准）。

### [ ] 4. PDU byte[] setter 用平台默认字符集

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/pdu/` 下多个 PDU（如 `report/CmsReport.java` L44/L62、`data/CmsGetDataDirectoryRequest.java` L29-L31、`data/sequence/data/CmsDataRefEntry.java` 等）
- **问题**：`new String(byte[])` 依赖平台默认字符集，跨平台行为不一致。
- **方案**：统一 `new String(v, StandardCharsets.UTF_8)`（及对应 `getBytes(StandardCharsets.UTF_8)`）。可写脚本批量替换，注意 ASN.1 VisibleString 为 8-bit 字符，UTF-8 兼容。
- **验证**：`mvn test -pl jcms-core`。

### [ ] 5. CmsChoice syncList*/syncArray* 四方法重复

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java`
- **位置**：L444-L454 vs L480-L489（syncToInner）；L457-L477 vs L492-L512（syncFromInner）
- **问题**：两对方法逐行相同，纯重复。
- **方案**：合并为单一实现，`Sync.LIST` 与 `Sync.ARRAY` 复用同一方法。
- **验证**：`mvn test -pl jcms-core`（重点覆盖含 LIST/ARRAY 变体的 CHOICE，如 CmsData）。

## 🟡 低优先级

### [ ] 6. CmsFormatUtil.bytesToHex 逐字节 String.format

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/util/CmsFormatUtil.java`
- **位置**：L152-L157
- **问题**：每字节 `String.format("%02x")` 创建 Formatter，性能差。
- **方案**：复用 `InnerBase.hex()`（256 项查表，大写），或本地查表。

### [ ] 7. CmsEnum.value()/value(int) 每次反射查 ValueRange

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsEnum.java`
- **位置**：L53、L63
- **问题**：每次取值都 `getClass().getAnnotation(ValueRange.class)`。
- **方案**：ClassValue 缓存 `ValueRange`，或在子类构造时固化 min/max。

### [ ] 8. CmsFormatUtil 失效/残留代码

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/util/CmsFormatUtil.java`
- **位置**：L26-L28（`toString(CmsType)` 纯委托）；L129-L137（`scalarToJson` 反射 `getField("value")`，Inner\* 只有 `_v`，恒输出 null）；L120（`"innerCache"` 过时字段名）
- **方案**：删除 `toString` 委托与死方法；`scalarToJson` 改用 `V.getVal(type.inner._v)`；`collectFields` 移除 `innerCache` 检查。

### [ ] 9. CmsType.decode() 异常无上下文

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsType.java`
- **位置**：L62-L69
- **问题**：反射失败只抛 `RuntimeException(e)`，无类名/数据信息，排障困难。
- **方案**：包装时附带 `inner.getClass().getSimpleName()` 与数据长度（`data != null ? data.length : -1`）。

---

## 📌 已排查（验证器判定非缺陷，可选加固）

### [ ] 10. CmsSetDataValuesError / CmsSetDataSetValuesError 的 syncToInner 类型污染

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/pdu/data/CmsSetDataValuesError.java`、`jcms/jcms-core/src/main/java/com/ysh/jcms/pdu/dataset/CmsSetDataSetValuesError.java`
- **位置**：两文件 syncToInner()
- **说明**：`inner._v.get("result")` 强转 `List<InnerBase>` 后 clear+add，decode 后该列表运行时是 `ArrayList<Integer>`，泛型擦除使 add 不抛 CCE、encode 经 toJson 可正常工作，但列表元素类型被污染，未来按 `List<Integer>` 读取会抛 CCE。
- **方案（可选）**：syncToInner 改为重建列表（`inner._v.put("result", new ArrayList<>(...))`），与 syncFromInner 的 `List<Object>` 防御对齐。

---

## 验证命令

```bash
cd /media/psf/Home/workspace/project/dlt2811bean/jcms
mvn test -pl jcms-core            # 全量
mvn test -pl jcms-core -Dtest="*Data*"   # 数据包
mvn test -pl jcms-data            # jcms-data 受影响时
```
