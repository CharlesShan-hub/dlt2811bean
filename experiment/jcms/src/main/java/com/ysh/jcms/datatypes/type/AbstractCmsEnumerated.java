package com.ysh.jcms.datatypes.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCmsEnumerated<T extends AbstractCmsEnumerated<T>>
        extends AbstractCmsScalar<T, Integer> implements CmsEnumerated<T> {

    protected final int size;

    protected AbstractCmsEnumerated(String typeName, int value, int size) {
        super(typeName, 0);
        this.size = size;
        set(value);
    }

    @Override
    public void set(Integer value) {
        if (value < 0 || value >= size) {
            throw new IllegalArgumentException("value " + value + " out of range [0, " + (size - 1) + "]");
        }
        super.set(value);
    }

    @Override
    public boolean is(int value) {
        return this.value == value;
    }

    @Override
    public String toString() {
        String name = nameOf(value);
        if (name != null) {
            return "(" + getClass().getSimpleName() + ") " + name + "(" + value + ")";
        }
        return super.toString();
    }

    private String nameOf(int val) {
        try {
            Map<Integer, String> mapping = nameCache();
            String name = mapping.get(val);
            if (name != null) return name;
        } catch (Exception ignored) {
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, String> nameCache() {
        Map<Integer, String> cache = (Map<Integer, String>) cacheGet();
        if (cache != null) return cache;
        cache = buildNameMap();
        cachePut(cache);
        return cache;
    }

    private Map<Integer, String> buildNameMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Field field : getClass().getFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod) && field.getType() == int.class) {
                try {
                    int val = field.getInt(null);
                    map.put(val, field.getName());
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return map;
    }

    // ==================== Per-class cache ====================

    private static final java.util.Map<Class<?>, Object> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    private Object cacheGet() {
        return CACHE.get(getClass());
    }

    private void cachePut(Object cache) {
        CACHE.put(getClass(), cache);
    }
}
