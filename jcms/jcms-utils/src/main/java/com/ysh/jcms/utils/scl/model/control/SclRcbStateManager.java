package com.ysh.jcms.utils.scl.model.control;

import com.ysh.jcms.data.block.CmsBrcb;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Runtime state manager for BRCB/URCB values modified via SetBRCBValues/SetURCBValues.
 *
 * <p>Stores a mutable {@link CmsBrcb} per reference. When GetBRCBValues/GetURCBValues
 * is called, the runtime state (if present) takes precedence over static SCL defaults.
 */
public class SclRcbStateManager {

    private static final ConcurrentMap<String, CmsBrcb> state = new ConcurrentHashMap<>();

    private SclRcbStateManager() {}

    /** Get runtime state for a ref, or null if never set. */
    public static CmsBrcb get(String ref) {
        return state.get(ref);
    }

    /** Get or create runtime state for a ref. */
    public static CmsBrcb getOrCreate(String ref) {
        return state.computeIfAbsent(ref, k -> new CmsBrcb());
    }

    /** Remove runtime state for a ref. */
    public static void remove(String ref) {
        state.remove(ref);
    }

    /** Clear all runtime state. */
    public static void clear() {
        state.clear();
    }
}
