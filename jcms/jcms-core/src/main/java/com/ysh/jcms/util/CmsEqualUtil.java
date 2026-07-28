package com.ysh.jcms.util;

import com.ysh.jcms.core.CmsType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Utility for hierarchical comparison of two CmsType objects by walking their
 * innerCache trees + public CmsType/List fields.
 *
 * <p>This avoids the null-vs-default-value problem in Lombok-generated
 * {@code Inner*.equals()} after encode/decode roundtrips, because it only
 * compares what was actually set (innerCache entries), not every Inner* field.
 */
public class CmsEqualUtil {

    /**
     * Compare two CmsType objects for logical equality.
     *
     * <p>Compares innerCache maps (CmsScalar values, CmsSequence @CmsField wrappers)
     * and public CmsType / List fields (CmsChoice variants, SEQUENCE OF).
     */
    public static boolean equal(CmsType a, CmsType b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.getClass() != b.getClass()) return false;

        // 1. Compare innerCache maps (CmsScalar values, CmsSequence @CmsField wrappers)
        if (!compareValue(a.innerCache, b.innerCache)) {
            System.err.println("=== CmsEqualUtil DEBUG " + a.getClass().getSimpleName() + " ===");
            System.err.println("a.innerCache: " + a.innerCache);
            System.err.println("b.innerCache: " + b.innerCache);
            for (String k : a.innerCache.keySet()) {
                if (isPresenceKey(k)) continue;
                if (!b.innerCache.containsKey(k)) {
                    System.err.println("  key only in a: " + k);
                } else if (!compareValue(a.innerCache.get(k), b.innerCache.get(k))) {
                    System.err.println("  diff key: " + k + " a=" + a.innerCache.get(k) + " b=" + b.innerCache.get(k));
                }
            }
            for (String k : b.innerCache.keySet()) {
                if (isPresenceKey(k)) continue;
                if (!a.innerCache.containsKey(k)) {
                    System.err.println("  key only in b: " + k);
                }
            }
            return false;
        }

        // 2. Compare public CmsType / List fields (CmsChoice variant wrappers, SEQUENCE OF)
        try {
            for (Field f : a.getClass().getFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                String name = f.getName();
                if ("inner".equals(name) || "innerCache".equals(name)) continue;
                Class<?> type = f.getType();
                if (CmsType.class.isAssignableFrom(type) || List.class.isAssignableFrom(type)) {
                    Object va = f.get(a);
                    Object vb = f.get(b);
                    if (!compareValue(va, vb)) return false;
                }
            }
        } catch (IllegalAccessException e) {
            return false;
        }

        return true;
    }

    // ── recursive value comparison ──────────────────────────────────

    @SuppressWarnings("unchecked")
    private static boolean compareValue(Object va, Object vb) {
        if (va == vb) return true;
        if (va == null || vb == null) return false;
        if (va.getClass() != vb.getClass()) return false;

        if (va instanceof Map) {
            return compareMaps((Map<String, Object>) va, (Map<String, Object>) vb);
        }
        if (va instanceof List) {
            return compareLists((List<Object>) va, (List<Object>) vb);
        }
        if (va instanceof byte[]) {
            boolean eq = Arrays.equals((byte[]) va, (byte[]) vb);
            if (!eq) {
                byte[] ba = (byte[])va, bb = (byte[])vb;
                System.err.println("  byte[] diff: len a=" + ba.length + " b=" + bb.length
                    + " a=" + java.util.Arrays.toString(ba) + " b=" + java.util.Arrays.toString(bb));
            }
            return eq;
        }
        if (va instanceof CmsType) {
            return equal((CmsType) va, (CmsType) vb);
        }
        return va.equals(vb);
    }

    private static boolean compareMaps(Map<String, Object> ma, Map<String, Object> mb) {
        // Compare only non-presence keys (skip hasXxx — they are sync-internal)
        for (Map.Entry<String, Object> e : ma.entrySet()) {
            String key = e.getKey();
            if (isPresenceKey(key)) continue;
            Object va = e.getValue();
            Object vb = mb.get(key);
            if (!compareValue(va, vb)) return false;
        }
        for (String key : mb.keySet()) {
            if (isPresenceKey(key)) continue;
            if (!ma.containsKey(key)) return false;
        }
        return true;
    }

    /** Detect {@code hasXxx} presence keys used by CmsSequence's OPTIONAL tracking. */
    private static boolean isPresenceKey(String key) {
        return key.startsWith("has") && key.length() > 3
            && Character.isUpperCase(key.charAt(3));
    }

    private static boolean compareLists(List<Object> la, List<Object> lb) {
        if (la.size() != lb.size()) return false;
        for (int i = 0; i < la.size(); i++) {
            if (!compareValue(la.get(i), lb.get(i))) return false;
        }
        return true;
    }
}
