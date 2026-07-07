package com.ysh.jcms.app.handler.msv;

import com.ysh.jcms.data.block.CmsMsvcb;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory cache for MSVCB values.
 *
 * <p>SetMSVCBValues writes to this cache; GetMSVCBValues reads from it
 * (falling back to SCL if not present).  Changes are lost on restart.
 */
public class MsvcbCache {

    private static final ConcurrentMap<String, CmsMsvcb> cache = new ConcurrentHashMap<>();

    public static CmsMsvcb get(String ref) {
        return cache.get(ref);
    }

    public static void put(String ref, CmsMsvcb msvcb) {
        cache.put(ref, msvcb);
    }
}
