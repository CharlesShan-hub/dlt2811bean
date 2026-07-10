package com.ysh.jcms.app.handler.goose;

import com.ysh.jcms.data.block.CmsGoCb;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory cache for GoCB values.
 *
 * <p>
 * SetGoCBValues writes to this cache; GetGoCBValues reads from it (falling back
 * to SCL if not present). Changes are lost on restart.
 */
public class GoCbCache {

    private static final ConcurrentMap<String, CmsGoCb> cache = new ConcurrentHashMap<>();

    public static CmsGoCb get(String ref) {
        return cache.get(ref);
    }

    public static void put(String ref, CmsGoCb gocb) {
        cache.put(ref, gocb);
    }
}
