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

### [x] 2. InnerBase.toJson 将纯 hex 字符串大写化（编码值失真）— ✅ 已完成 2026-08-01

- **文件**：`csasn1/src/generator/java/templates/Base.java.txt`（生成源头，jcms-data 由 csasn1 生成）
- **依据**：`assets/standard-2811.md`
- **问题**：`toJson` 对全 hex 字符的字符串（如 `"abc123"`）执行 `toUpperCase()`，encode 路径会改写用户设置的字符串值。
- **修复（A 组模板）**：
  1. **A1** `toJson` String 分支改为原样返回（不再大写化）；
  2. **A2** `bitStringHex` LOWER→UPPER（与 Rust JER 一致，保证 BIT STRING 往返 equals 不因大小写失败），并删除 `HEX_BYTES_LOWER`/`HEX_DIGITS_LOWER` 死代码；
  3. **A4** `unhex` 单遍解析跳过 `0x`/`0X` 前缀（原 `replace` 每次分配 2 个临时串）；
  4. **A6（已回退）** test_struct.rs 加 decode/assert 往返断言 → 默认构造 SEQUENCE 违反 ASN.1 约束（空 SEQUENCE OF、空定长串），159 个测试 decode 失败，已回退并加注释说明。
- **未做**：A3 equals/hashCode 缓存（`_v` 可变，失效难控，风险>收益）；A5 encode 上收基类（重构风险中高，收益小）。
- **验证**：`--prefix Inner` 生成到临时目录，`mvn test` **367 tests, 0 errors** ✅（用户需重跑 `just gen-jcms-data` 让改动生效）

## 🟠 中优先级

### [ ] 3. InnerBase.equals/hashCode 每次完整 JSON 序列化

- **文件**：`jcms/jcms-data/src/main/java/com/ysh/jcms/data/InnerBase.java`
- **位置**：L72-L82；下游 `CmsType.equals/hashCode`（jcms-core `data/core/CmsType.java` L85-L95）委托 inner
- **问题**：每次比较/哈希都全树 `toJson(toJsonValue())` 重建临时对象，测试断言与 contains/Map key 开销大。
- **方案**：惰性缓存规范化 JSON 串（`_v` 可变，在 syncToInner/decode/rebind 后置 null 失效）；`hashCode` 缓存 int 而非每次序列化。
- **验证**：现有 300+ 测试全绿，性能可感知提升（可加微基准）。

### [x] 4. PDU byte[] setter 用平台默认字符集 — ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/pdu/` 下多个 PDU
- **问题**：`new String(byte[])` 依赖平台默认字符集，跨平台行为不一致。
- **修复**：批量替换 92 处 `new String(byte[])` → `new String(v, StandardCharsets.UTF_8)`（含三元形式），并补齐 `import java.nio.charset.StandardCharsets;`。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

### [x] 5. CmsChoice syncList*/syncArray* 四方法重复 — ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsChoice.java`
- **问题**：两对方法逐行相同，纯重复。
- **修复**：删除 `syncArrayToInner`/`syncArrayFromInner`，`Sync.ARRAY` 与 `Sync.LIST` 复用 `syncListToInner`/`syncListFromInner`。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

## 🟡 低优先级

### [x] 6. CmsFormatUtil.bytesToHex 逐字节 String.format — ✅ 已完成 2026-08-01（随 #8 一并删除）

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/util/CmsFormatUtil.java`
- **修复**：`bytesToHex` 与整棵树渲染功能（toString/toJson/collectFields/scalarToJson 等）均无调用者，已全部删除，`CmsFormatUtil` 精简为仅保留 `escapeJson`（jcms-app 唯一使用的方法）。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

### [x] 7. CmsEnum.value()/value(int) 每次反射查 ValueRange — ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsEnum.java`
- **修复**：`ClassValue<ValueRange> VALUE_RANGE` 缓存，`value()`/`value(int)` 改用 `VALUE_RANGE.get(getClass())`。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

### [x] 8. CmsFormatUtil 失效/残留代码 — ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/util/CmsFormatUtil.java`
- **修复**：grep 确认 jcms-app 仅使用 `escapeJson`；`toString(CmsType)` 纯委托、`scalarToJson` 反射 "value" 恒 null、`collectFields` "innerCache" 残留等全部删除，类精简为仅 `escapeJson`。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

### [x] 9. CmsType.decode() 异常无上下文 — ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/data/core/CmsType.java`
- **修复**：包装时附带 `inner.getClass().getSimpleName()` 与 `dataLen`。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

---

## 📌 已排查（验证器判定非缺陷，可选加固）

### [x] 10. CmsSetDataValuesError / CmsSetDataSetValuesError 的 syncToInner 类型污染 — ✅ 已完成 2026-08-01

- **文件**：`jcms/jcms-core/src/main/java/com/ysh/jcms/pdu/data/CmsSetDataValuesError.java`、`jcms/jcms-core/src/main/java/com/ysh/jcms/pdu/dataset/CmsSetDataSetValuesError.java`
- **修复**：`syncToInner` 改为重建列表（`inner._v.put("result", newList)`），不再对 decode 后的 raw `List<Integer>` 做 clear+add 污染元素类型。
- **验证**：`mvn test -pl jcms-core` BUILD SUCCESS ✅

---

## 验证命令

```bash
cd /media/psf/Home/workspace/project/dlt2811bean/jcms
mvn test -pl jcms-core            # 全量
mvn test -pl jcms-core -Dtest="*Data*"   # 数据包
mvn test -pl jcms-data            # jcms-data 受影响时
```
