# jcms-config — 配置模块

## 职责

jcms-config 提供全局配置的**定义、加载、注入**能力。所有可配置参数集中在一个 POJO 模型中，通过 YAML 文件加载，并支持按需注入到任意业务对象中。

一句话概括：**CMS 项目的集中式配置框架**。

> 不是AI的总结：jcms-config本意是模仿springboot的注解形式的配置，但是实现过程中，还是采用了非注解访问的形式。总的来讲这个模块就是用来加载配置的。后期要修改或者增加配置，就要修改application.yaml然后修改CmsConfig等等内容。

## 架构

```
application.yaml          ← 配置文件
      │
      ▼
CmsConfigLoader           ← 加载器（文件系统 → classpath → 默认值）
      │
      ▼
    CmsConfig             ← 配置数据模型（POJO 树）
      │
      ▼
CmsConfigInjector         ← 反射注入器（@CmsValue 注解）
      │
      ▼
  业务对象                  ← Handler、Service 等
```

## 类详解

### 1. `CmsConfig` — 配置数据模型

包路径：`com.ysh.jcms.utils.config.CmsConfig`

一个**纯 POJO**，使用嵌套静态类组织 4 大配置域：

```
CmsConfig
├── Server           — 服务端配置
│   ├── port             (int, 默认 8102)        — 明文端口
│   ├── sslPort          (int, 默认 9102)        — TLS 端口
│   ├── testSclFiles     (List<String>)          — 测试用 SCD 文件列表
│   ├── sclFiles         (List<String>)          — 正式 SCD 文件列表
│   └── KeepAlive        — 服务端保活
│       ├── idleTimeoutMs   (int, 默认 30000)
│       ├── retryIntervalMs (int, 默认 5000)
│       └── maxRetries      (int, 默认 4)
│
├── Client           — 客户端配置
│   ├── defaultIedName    (String, 默认 "E1Q1SB1")
│   ├── defaultAccessPoint(String, 默认 "S1")
│   ├── defaultSecure     (boolean, 默认 false)
│   ├── connectTimeoutMs  (int, 默认 5000)
│   ├── requestTimeoutMs  (int, 默认 5000)
│   └── Console           — 交互式 CLI 配置
│       ├── tracePdu         (boolean, 默认 false)  — PDU 跟踪开关
│       ├── autoExec         (String)                — 启动时自动执行命令
│       ├── showAutoExec     (boolean, 默认 true)
│       ├── showConnectHint  (boolean, 默认 true)
│       ├── apiEnabled       (boolean, 默认 true)    — HTTP API 开关
│       ├── apiPort          (int, 默认 7899)
│       └── apiHost          (String, 默认 "http://127.0.0.1")
│
├── Protocol         — 协议配置
│   ├── maxArraySize     (int, 默认 1024)        — 数组最大元素数
│   ├── gbkToUtf8        (boolean, 默认 false)   — GBK→UTF-8 转换
│   ├── Negotiate        — 协商参数
│   │   ├── apduSize        (int, 默认 65535)
│   │   ├── asduSize        (int, 默认 65531)
│   │   ├── protocolVersion (int, 默认 1)
│   │   └── modelVersion    (String, 默认 "1.0")
│   ├── File             — 文件服务根路径
│   │   └── rootPath        (String, 默认 "config/files")
│   ├── Log              — 日志存储根路径
│   │   └── rootPath        (String, 默认 "config/logs")
│   ├── Setting          — 定值组默认参数
│   │   ├── numOfSG          (int, 默认 4)
│   │   ├── sgDefaultEnabled (boolean, 默认 true)
│   │   └── sgDefaultName    (String, 默认 "SG1")
│   └── Dataset          — 数据集配置
│       └── setDataSetPersistent (boolean, 默认 false)
│
└── Security         — 安全配置
    ├── enabled        (boolean, 默认 false)
    ├── Keystore
    │   ├── path          (String, 默认 "certs/server.pfx")
    │   └── password      (String, 默认 "changeit")
    └── Truststore
        ├── path          (String, 默认 "certs/ca.cer")
        └── password      (String, 默认 "changeit")
```

#### merge() 方法

`merge(CmsConfig other)` 支持将加载的配置合并到默认配置中。合并策略：

- **仅覆盖非默认值**：只当 `other` 中的值不等于默认值时才会覆盖
- **安全相关特殊处理**：`security.enabled` 只要 `other` 中为 true 就覆盖（不判断默认值）
- **列表类型**：testSclFiles 和 sclFiles 会整体替换

***

### 2. `CmsConfigLoader` — 配置加载器

包路径：`com.ysh.jcms.utils.config.CmsConfigLoader`

负责从以下来源加载配置（优先级递减）：

```
1. 文件系统               → application.yaml, config/application.yaml, conf/application.yaml
2. Classpath              → classpath:application.yaml
3. 默认值                 → CmsConfig 构造函数中的默认值
4. 系统属性覆盖            → -Dcms.server.port=8080 -Dcms.server.testSclFile=...
```

关键行为：

- **单例缓存**：`load()` 首次调用后缓存结果，后续返回同一实例；`reload()` 清空缓存重新加载
- **YAML 解析**：使用 SnakeYAML 直接反序列化为 `CmsConfig` 对象
- **系统属性覆盖**：当前支持 `cms.server.port` 和 `cms.server.testSclFile`

***

### 3. `CmsValue` — 注入注解

包路径：`com.ysh.jcms.utils.config.CmsValue`

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CmsValue {
    String value();   // 配置路径，如 "server.port"
}
```

***

### 4. `CmsConfigInjector` — 配置注入器

包路径：`com.ysh.jcms.utils.config.CmsConfigInjector`

通过反射将配置值注入到任意对象的 `@CmsValue` 字段中，避免手动传递配置参数。

注入流程：

```
1. 遍历目标对象的所有字段
2. 查找 @CmsValue 注解
3. 按注解路径（如 "server.keepalive.idleTimeoutMs"）从 CmsConfig 树中取值
4. 类型转换（int / long / boolean / String）
5. 反射赋值
```

示例：

```java
public class SomeHandler {
    @CmsValue("server.keepalive.idleTimeoutMs")
    private int idleTimeoutMs;

    public SomeHandler() {
        CmsConfigInjector.inject(this);
    }
}
```

***

## 使用方式

### 配置文件示例（application.yaml）

```yaml
server:
  port: 8102
  sslPort: 9102
  testSclFiles:
    - config/sample-scd-full.scd
  keepalive:
    idleTimeoutMs: 30000
    retryIntervalMs: 5000
    maxRetries: 4

client:
  defaultIedName: "E1Q1SB1"
  defaultAccessPoint: "S1"
  defaultSecure: false
  connectTimeoutMs: 5000
  requestTimeoutMs: 5000
  console:
    tracePdu: false
    autoExec: ""
    apiEnabled: true
    apiPort: 7899
    apiHost: "http://127.0.0.1"

protocol:
  maxArraySize: 1024
  gbkToUtf8: false
  negotiate:
    apduSize: 65535
    asduSize: 65531
    protocolVersion: 1
  file:
    rootPath: "config/files"
  log:
    rootPath: "config/logs"

security:
  enabled: false
  keystore:
    path: "certs/server.pfx"
    password: "changeit"
```

### 编程方式访问

```java
// 获取全局配置
CmsConfig config = CmsConfigLoader.load();
int port = config.getServer().getPort();
boolean tracePdu = config.getClient().getConsole().isTracePdu();
```

***

## 与上层模块的关系

| 模块                       | 使用方式                                      |
| ------------------------ | ----------------------------------------- |
| **jcms-app/server**      | `CmsConfigLoader.load()` 读取端口、SCD 文件、保活参数 |
| **jcms-app/client**      | 读取默认 IED/AP、超时、控制台参数                      |
| **jcms-app/console**     | `@CmsValue` 注入 tracePdu、autoExec、API 配置   |
| **jcms-utils/transport** | 读取 negotiate 参数、安全配置                      |
| **jcms-utils/scl**       | 读取 SCD 文件路径                               |
| **jcms-utils/security**  | 读取 keystore/truststore 路径                 |

