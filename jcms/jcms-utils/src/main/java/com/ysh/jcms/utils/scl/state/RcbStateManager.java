package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.data.sequence.block.CmsBrcb;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * BRCB/URCB 运行时状态管理器。
 * <p>
 * 线程安全的 BRCB 状态存储。SetBRCBValues/SetURCBValues 写入， GetBRCBValues/GetURCBValues
 * 读取（优先于静态 SCL 默认值）。
 */
public final class RcbStateManager {

    private static final ConcurrentMap<String, CmsBrcb> state = new ConcurrentHashMap<>();

    private RcbStateManager() {
    }

    /** 获取指定引用的运行时状态，未设置返回 null。 */
    public static CmsBrcb get(String ref) {
        return state.get(ref);
    }

    /** 获取或创建指定引用的运行时状态。 */
    public static CmsBrcb getOrCreate(String ref) {
        return state.computeIfAbsent(ref, k -> new CmsBrcb());
    }

    /** 移除指定引用的运行时状态。 */
    public static void remove(String ref) {
        state.remove(ref);
    }

    /** 清除所有运行时状态。 */
    public static void clear() {
        state.clear();
    }
}
