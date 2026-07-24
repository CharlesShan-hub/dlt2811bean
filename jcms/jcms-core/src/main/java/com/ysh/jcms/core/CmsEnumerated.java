package com.ysh.jcms.core;

import com.ysh.jcms.core.NativeBridge.Codec;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enumerated ::= Int8 (-128..127) — 7.1.6 PER: constrained integer, 8 bits
 * sizeof = 4
 *
 * Base class for all ENUMERATED / coded-enum types: CmsDbpos, CmsTcmd,
 * CmsServiceError, CmsOrCat, etc.
 */
public class CmsEnumerated extends CmsTypeOld {

    private static final Map<Class<?>, Map<Integer, String>> CONSTANT_NAMES = new ConcurrentHashMap<>();

    private int value;

    public CmsEnumerated() {
        super(Codec.ENUMERATED);
    }
    public CmsEnumerated(int value) {
        super(Codec.ENUMERATED);
        this.value = value;
        write();
    }

    /** Constructor with range validation. Subclasses pass (min, max, value). */
    public CmsEnumerated(int min, int max, int value) {
        super(Codec.ENUMERATED);
        if (value < min || value > max)
            throw new IllegalArgumentException(getClass().getSimpleName() + " out of range [" + min + "," + max + "]: " + value);
        this.value = value;
        write();
    }

    public int value() {
        return value;
    }
    public CmsEnumerated value(int v) {
        this.value = v;
        write();
        return this;
    }

    /**
     * 返回当前值对应的常量名，如 CmsServiceError(12) → "FAILED_DUE_TO_SERVER_CONSTRAINT"。
     * 通过反射子类的 public static final int 常量自动推导，无需手动维护映射。
     */
    public String constantName() {
        Class<?> clazz = getClass();
        Map<Integer, String> names = CONSTANT_NAMES.get(clazz);
        if (names == null) {
            names = buildConstantMap(clazz);
            CONSTANT_NAMES.put(clazz, names);
        }
        return names.getOrDefault(value, "UNKNOWN(" + value + ")");
    }

    private static Map<Integer, String> buildConstantMap(Class<?> clazz) {
        Map<Integer, String> map = new HashMap<>();
        for (Field field : clazz.getFields()) {
            int mod = field.getModifiers();
            if (!Modifier.isStatic(mod) || !Modifier.isPublic(mod))
                continue;
            if (field.getType() != int.class)
                continue;
            try {
                int val = field.getInt(null);
                map.put(val, field.getName());
            } catch (Exception ignored) {
            }
        }
        return map;
    }

    @Override
    public String toString() {
        return constantName() + " (" + value + ")";
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }

    @Override
    public void write() {
        nativePtr.setInt(0, value);
    }

    @Override
    public void read() {
        this.value = nativePtr.getInt(0);
    }
}
