package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.core.data.core.CmsSequence;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * ASSOCIATION 层控制块状态存储：按会话（sessionId）隔离的并发存储。
 * <p>
 * 生命周期：连接断开即清除（{@link #removeSession(String)}），对应 URCB 等 per-association
 * 实例字段（{@code @CbField(scope = CbFieldScope.ASSOCIATION)}）。
 */
public final class CbAssociationStore<T extends CmsSequence> {

    private final ConcurrentMap<String, ConcurrentMap<String, T>> bySession = new ConcurrentHashMap<>();

    private ConcurrentMap<String, T> bucket(String sessionId) {
        return bySession.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
    }

    /** 获取指定会话下指定引用的状态，未设置返回 {@code null}。 */
    public T get(String sessionId, String ref) {
        ConcurrentMap<String, T> b = bySession.get(sessionId);
        return b != null ? b.get(ref) : null;
    }

    /** 获取或创建指定会话下指定引用的状态。 */
    public T getOrCreate(String sessionId, String ref, Supplier<T> factory) {
        return bucket(sessionId).computeIfAbsent(ref, k -> factory.get());
    }

    /** 写入/替换指定会话下指定引用的状态。 */
    public void put(String sessionId, String ref, T cb) {
        bucket(sessionId).put(ref, cb);
    }

    /** 移除指定会话下指定引用的状态。 */
    public void remove(String sessionId, String ref) {
        ConcurrentMap<String, T> b = bySession.get(sessionId);
        if (b != null) {
            b.remove(ref);
        }
    }

    /** 移除整个会话的全部关联级状态（关联释放钩子）。 */
    public void removeSession(String sessionId) {
        bySession.remove(sessionId);
    }

    /** 清空所有会话的状态。 */
    public void clear() {
        bySession.clear();
    }
}
