package com.ysh.jcms.core.data.sequence.block;

/**
 * 控制块字段的生命周期层级（IEC 61850-7-2 配置属性/运行属性分类的显性化）。
 * <p>
 * 同一控制块内不同字段横跨不同层级，例如 BRCB 的 rptID 属 ENGINEERING，rptEna 属 RUNTIME。
 * <ul>
 * <li><b>ENGINEERING</b> — 工程配置：由 SCL 定义，跨重启生效；运行时 Set 可覆盖（覆盖值存 RUNTIME 层）</li>
 * <li><b>RUNTIME</b> — 进程内运行状态：Set 可改，客户端断开不丢，服务器重启丢失</li>
 * <li><b>ASSOCIATION</b> — 关联级：URCB 等 per-association 实例的字段，连接断开即清除</li>
 * </ul>
 */
public enum CbFieldScope {
    ENGINEERING, RUNTIME, ASSOCIATION
}
