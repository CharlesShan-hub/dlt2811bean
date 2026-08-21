# jcms-config — 配置模块

## 设计思路

jcms-config 是项目的**集中式配置**：所有可调参数收在一个 `CmsConfig` POJO 里，从 `application.yaml` 加载，业务代码按需读取。

它模仿 Spring Boot 的配置风格，但**没有用 Spring 容器**，只是借用了两个概念：

1. **POJO 配置模型**：配置就是一棵嵌套的 Java 对象树，字段带默认值。
2. **`@CmsValue` 注入**：仿 `@Value("${...}")`，用注解把某个配置项直接注入到字段，而不是层层手动传参。

所以叫 `CmsValue`，就是「Cms 版的 `@Value`」。

## 配置文件在哪

按以下顺序查找，命中即停（都在进程工作目录或 classpath）：

```
1. ./application.yaml
2. ./config/application.yaml
3. ./conf/application.yaml
4. classpath:application.yaml
5. 都没找到 → 用 CmsConfig 里的默认值
```

加载后还会检查系统属性 `-Dcms.server.port=...`、`-Dcms.server.testSclFile=...`，若存在则覆盖。

## 怎么写配置

`application.yaml` 结构就是 `CmsConfig` 的对象树，缩进即层级：

```yaml
server:
  port: 8102
  sslPort: 9102
  sclFiles:
    - config/sample-scd-full.scd
  keepalive:
    idleTimeoutMs: 30000

client:
  defaultIedName: "E1Q1SB1"
  defaultAccessPoint: "S1"

protocol:
  negotiate:
    apduSize: 65535
    asduSize: 65531

security:
  enabled: false
```

没写的字段就落在默认值上。加载用 `merge()` 合并：**只覆盖「不等于默认值」的项**，所以配置里可以只写要改的那几行。

## 怎么读（两种方式）

### 方式一：编程式（主动取）

拿到全局单例配置对象，直接链式访问：

```java
CmsConfig config = CmsConfigLoader.load();   // 首次加载后缓存，reload() 清缓存
int port = config.server().port();
boolean tracePdu = config.client().console().tracePdu();
```

适合「一次性读几项」的场景（server/client 启动时）。

### 方式二：注解注入（声明式）

字段上加 `@CmsValue("路径")`，构造器里调一次 `CmsConfigInjector.inject(this)`，框架反射赋值：

```java
public class SomeHandler {
    @CmsValue("server.keepalive.idleTimeoutMs")
    private int idleTimeoutMs;

    public SomeHandler() {
        CmsConfigInjector.inject(this);   // 按注解路径取值并注入
    }
}
```

`@CmsValue` 的 `value()` 是**从 `CmsConfig` 根开始、用 `.` 分隔的路径**（对应 `server().keepalive().idleTimeoutMs()`），注入器会按路径逐级反射取值，并做 `int/long/boolean/String/枚举` 类型转换。

适合「某几个字段散落在业务对象里」的场景（console/handler）。

## 小结

- 配置集中在一个 `CmsConfig` POJO + 一个 `application.yaml`。
- 读配置只有两条路：**主动 `load()` 取对象**，或**注解 `@CmsValue` 注入字段**。
- 设计上模仿 Spring Boot 的「配置模型 + `@Value`」，但零依赖、零容器，纯反射 + SnakeYAML 实现。
