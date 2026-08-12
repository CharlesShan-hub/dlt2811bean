package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.core.CmsSequence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * RUNTIME 层控制块状态存储：进程内按 ref 隔离的并发存储。
 * <p>
 * 生命周期：Set 写入、Get 优先读取；客户端断开不丢，服务器重启丢失（回到 SCL 工程值）。
 */
public final class CbStateStore<T extends CmsSequence> {

    private final ConcurrentMap<String, T> state = new ConcurrentHashMap<>();

    /** 获取指定引用的运行时状态，未设置返回 {@code null}。 */
    public T get(String ref) {
        return state.get(ref);
    }

    /** 获取或创建指定引用的运行时状态。 */
    public T getOrCreate(String ref, Supplier<T> factory) {
        return state.computeIfAbsent(ref, k -> factory.get());
    }

    /** 写入/替换指定引用的运行时状态。 */
    public void put(String ref, T cb) {
        state.put(ref, cb);
    }

    /** 移除指定引用的运行时状态。 */
    public void remove(String ref) {
        state.remove(ref);
    }

    /** 清空所有运行时状态。 */
    public void clear() {
        state.clear();
    }
}
