# jcms 编码规范

基于阿里巴巴 Java 开发手册（P3C） + Google Java Style，针对 CMS 项目裁剪。

> 各章节标记说明：**【强制】** 必须遵守，**【推荐】** 建议遵守。

---

## 1 命名风格

### 【强制】通用规则

| 元素 | 规范 | 正例 | 反例 |
|------|------|------|------|
| 类/接口 | UpperCamelCase | `AssociateServer`, `GmAuthenticator` | `associateServer`, `XMLService` |
| 方法 | lowerCamelCase | `execute()`, `validateAuthParam()` | `Execute()`, `Validateauthparam()` |
| 常量 | UPPER_SNAKE_CASE | `CHOICE_VISIBLE_STRING`, `MAX_ARRAY_SIZE` | `choiceVisibleString`, `max_array_size` |
| 字段/参数/局部变量 | lowerCamelCase | `currentSapRef`, `timeTolerance` | `current_sap_ref`, `a` |
| 包名 | 全小写，点分隔，单数 | `com.ysh.jcms.utils.security` | `com.ysh.jcms.Utils` |

### 【强制】禁止拼音或中文

严禁使用拼音与英文混合或直接使用中文。

```java
// ❌ 错误
int 某变量 = 3;
String biaoming = "表名";
createBaowen();  // 应为 createMessage()

// ✅ 正确
String tableName = "LD0";
createMessage();
```

### 【强制】抽象类 / 异常类 / 测试类

| 类别 | 命名规则 | 示例 |
|------|----------|------|
| 抽象类 | `Abstract*` 或 `Base*` 开头 | `BaseServerHandler`, `BaseClientHandler` |
| 异常类 | `*Exception` 结尾 | `CmsParseException` |
| 测试类 | `*Test` 结尾 | `AssociateLoopbackTest` |

### 【强制】数组定义

类型与中括号紧挨。

```java
// ✅ 正确
int[] arrayDemo;
String[] args;

// ❌ 错误
int arrayDemo[];
String args[];
```

### 【强制】POJO 布尔字段不加 is 前缀

```java
// ❌ 错误 — 序列化框架会误以为字段名是 deleted
private boolean isDeleted;
public boolean isDeleted() { return isDeleted; }

// ✅ 正确
private boolean deleted;
public boolean isDeleted() { return deleted; }
```

### 【推荐】专有命名（本项目）

| 模式 | 用途 | 示例 |
|------|------|------|
| `*Server` | 服务端 handler | `GetGoCbValuesServer` |
| `*Client` | 客户端 handler | `GetGoCbValuesClient` |
| `*Console` | CLI 命令 | `GetGoCbValuesConsole` |
| `*Dao` | 数据传输对象 | `AssociateClientDao` |
| `Cms*` | 协议 PDU 类型 | `CmsAssociateRequest` |
| `Abstract*` / `Base*` | 抽象基类 | `BaseServerHandler` |

### 【推荐】设计模式体现

如果使用了设计模式，在类名中体现。

```java
public class GoCbCache;        // Cache 模式
public class ReportEngine;     // Engine 模式
public class AssociationIdGenerator;  // Generator 模式
```

### 【参考】枚举

枚举类名带 `Enum` 后缀，成员全大写，下划线分隔。

```java
public enum SessionStateEnum {
    DISCONNECTED,
    CONNECTED,
    ASSOCIATED
}
```

---

## 2 常量定义

### 【强制】魔法值禁止直接出现

```java
// ❌ 错误 — 12 的含义不明确
choice.altError.value(12);

// ✅ 正确 — 定义常量
public static final int ERR_TYPE_CONFLICT = 12;
choice.altError.value(ERR_TYPE_CONFLICT);
```

### 【推荐】常量归类

```java
// 接口内常量
public interface CmsServiceErrorConst {
    int NO_ERROR = 0;
    int INSTANCE_NOT_AVAILABLE = 2;
    int PARAMETER_VALUE_INAPPROPRIATE = 7;
}
```

---

## 3 代码格式

### 【强制】大括号

K&R 风格（左大括号不换行）。

```java
// ✅ 正确
if (condition) {
    doSomething();
} else {
    doOther();
}

// ✅ 方法声明
public void execute(String ref) throws Exception {
    send(ServiceName.SELECT, req);
}
```

### 【强制】缩进

- 使用 **2 个空格**（非 Tab）
- switch 块内缩进 2 个空格

### 【推荐】行宽

不超过 **120 字符**，超出换行。

```java
// ✅ 方法链换行
req.authParam(new CmsAuthenticationParameter()
        .cert(certBytes)
        .signedTime(new CmsUtcTime().now())
        .sigVal(signatureValue));

// ✅ 方法参数换行
return onDecodeError(reqId,
        CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
```

### 【强制】import 顺序

```
1. 静态 import (static)
    空行
2. com.ysh.jcms.*
    空行
3. 第三方 (lombok, slf4j, bcprov 等)
    空行
4. java.* / javax.*
```

各分组内按字母排序。

### 【推荐】@Override

所有重写方法必须加 `@Override`。

---

## 4 OOP 规范

### 【强制】禁止过时类

避免使用 `Vector`、`Hashtable`、`Stack`、`Enumeration` 等过时集合。

```java
// ❌ 错误
Vector<String> v = new Vector<>();

// ✅ 正确
List<String> list = new ArrayList<>();
```

### 【强制】equals 比较

已知对象在前，未知对象在后。

```java
// ✅ 正确
"OK".equals(status);
Integer.valueOf(0).equals(value);

// ❌ 可能 NPE
status.equals("OK");
```

### 【推荐】Lombok

配置类、POJO 类使用 `@Data` 简化。

```java
@Data
public class CmsConfig {
    private Server server = new Server();
    // 无需手写 getter/setter
}
```

---

## 5 异常处理

### 【强制】禁止吞掉异常

```java
// ❌ 错误 — 调用者毫不知情
try {
    send(service, req);
} catch (IOException e) {
    log.error("Send failed", e);
}

// ✅ 正确 — 包装抛出
try {
    send(service, req);
} catch (IOException e) {
    throw new RuntimeException("Send failed", e);
}
```

### 【强制】finally 中禁止 return

```java
// ❌ 错误 — finally 的 return 覆盖 try 的 return
try {
    return decodeResp(frame, resp);
} finally {
    return null;
}
```

### 【推荐】异常优先于错误码

只在跨模块的公共 API 层使用错误码（如 `CmsServiceError`），内部逻辑优先用异常。

---

## 6 日志规范

### 【强制】使用占位符

```java
// ✅ 正确
log.info("Associate request from {}: reqId={}", session.getSessionId(), reqId);

// ❌ 错误 — 即使不输出也已拼接
log.info("Associate request from " + session.getSessionId() + ": reqId=" + reqId);
```

### 【强制】禁止 System.out

生产代码中不应出现 `System.out.println()` 或 `System.err.println()`，统一使用 SLF4J。

### 【推荐】日志级别

| 级别 | 场景 |
|------|------|
| `ERROR` | 无法自动恢复的故障 |
| `WARN` | 非预期但系统可继续 |
| `INFO` | 关键业务流程节点 |
| `DEBUG` | 调试信息（生产不开启） |

---

## 7 并发处理

### 【强制】线程池

禁止使用 `Executors.newFixedThreadPool()`，必须使用 `ThreadPoolExecutor`。

```java
// ✅ 正确 — 显式参数
private final ExecutorService executor = new ThreadPoolExecutor(
    4, 8, 60, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(100),
    new ThreadPoolExecutor.AbortPolicy()
);
```

### 【强制】共享变量

多线程共享可变字段用 `ConcurrentHashMap`。

---

## 8 控制语句

### 【强制】if/else 必须用大括号

```java
// ✅ 即使单行也加大括号
if (condition) {
    return;
}
```

### 【强制】switch 必须有 default

```java
switch (val) {
    case 0: return "A";
    case 1: return "B";
    default: return "?";
}
```

---

## 9 注释规范

### 【强制】类注释

每个类写 Javadoc，说明职责。

```java
/**
 * 服务器关联处理器。
 *
 * <p>处理客户端 Associate 请求，包含国密认证校验。
 */
public class AssociateServer extends BaseServerHandler {
```

### 【强制】禁止无意义注释

```java
// ❌ 无意义
i++; // 加1

// ❌ 注释掉的代码 — 直接删除
// if (old) { doOldWay(); }
```

---

## 10 安全规范

### 【强制】TLS 证书验证

生产环境禁止使用 `trustAll()`，必须配置 CA 证书。

| 配置 | 用途 |
|------|------|
| `security.enabled: false` | 开发/测试，自签名 + trustAll |
| `security.enabled: true` | 生产，CA 校验 + 时间检查 |

### 【强制】防重放

应用层认证（Associate）必须带时间戳签名，服务端校验时间差 < `timeTolerance`。

```java
long diff = Math.abs(currentTime - signedTime);
if (diff > timeTolerance) {
    return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
}
```

默认 **300 秒（5 分钟）**，可通过 `security.timeTolerance` 配置。

---

## 11 项目结构

### 【推荐】目录结构

```
jcms-core/          协议 PDU 定义（Cms*Request/Response/Error）
jcms-utils/         工具类（config, scl, security, transport）
jcms-app/
  handler/          各服务的 Server/Client/Console 实现
    connection/      连接（associate, release, abort）
    data/            数据服务
    dataset/         数据集服务
    directory/       目录服务
    file/            文件服务
    goose/           GOOSE
    msv/             MSV
    control/         控制服务
    report/          报告服务
    rpc/             RPC
    sg/              定值组
    log/             日志
  console/           CLI 注册和交互
  node/              节点管理
```

### 【推荐】服务分包规范

每个服务三个核心文件：

| 文件 | 职责 | 注册位置 |
|------|------|----------|
| `*Server.java` | 服务端请求处理 | `CmsServerConsole` |
| `*Client.java` | 客户端请求发送 | `CmsClientConsole` |
| `*Console.java` | CLI 命令解析 | `CmsClientConsole` |

---

## 12 常见反例自查

| 问题 | 说明 | 涉及文件 |
|------|------|----------|
| magic number `12` | 应定义为常量 | `GetRpcMethodDefinitionServer.java:36` |
| catch 空处理 | 某些 catch 只打 log 未抛出 | 需全局扫描 |
| 布尔字段命名 | 检查是否有 `isXxx` 命名的布尔字段 | POJO 类 |
| 过时集合类 | 检查是否有 `Vector`/`Hashtable` | 需全局扫描 |

---

## 参考

- [阿里巴巴 P3C 命名风格](https://alibaba.github.io/p3c/%E7%BC%96%E7%A8%8B%E8%A7%84%E7%BA%A6/%E5%91%BD%E5%90%8D%E9%A3%8E%E6%A0%BC.html)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
