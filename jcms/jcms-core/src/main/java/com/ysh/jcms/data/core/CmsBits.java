package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Base class for BIT STRING types.
 *
 * <p>The packed value is stored in {@code inner._v} as a hex string (JER format).
 * Subclasses declare fields annotated with {@link Bit} for individual bit positions.
 */
public abstract class CmsBits extends CmsType {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Bit {
        int value();
        int length() default 1;
    }

    protected CmsBits(InnerBase inner) {
        super(inner);
    }

    /** Calculate total bit width from @Bit annotations. */
    private int bitCount() {
        int max = 0;
        for (Field f : getClass().getFields()) {
            Bit bit = f.getAnnotation(Bit.class);
            if (bit == null) continue;
            max = Math.max(max, bit.value() + bit.length());
        }
        return max > 0 ? max : 1;
    }

    /** Read packed value from _v (hex string → int). */
    private int readPacked() {
        Object v = inner._v.get("_");
        if (v instanceof String) {
            return InnerBase.parseBitStringHex((String) v, bitCount());
        }
        return 0;
    }

    /** Write packed value to _v (int → hex string). */
    private void writePacked(int v) {
        inner._v.put("_", InnerBase.bitStringHex(v, bitCount()));
    }

    private static int mask(int len) {
        return len >= 32 ? -1 : (1 << len) - 1;
    }

    public int value() {
        return readPacked();
    }

    public void value(int v) {
        writePacked(v);
    }

    public CmsBits value(CmsBits v) {
        value(v.value());
        return this;
    }

    @Override
    public void syncToInner() {
        int packed = 0;
        for (Field f : getClass().getFields()) {
            Bit bit = f.getAnnotation(Bit.class);
            if (bit == null) continue;
            try {
                int pos = bit.value();
                int len = bit.length();
                Class<?> type = f.getType();

                if (len == 1 && (type == boolean.class || type == Boolean.class)) {
                    if (f.getBoolean(this)) packed |= (1 << pos);
                } else if (len == 1 && CmsBoolean.class.isAssignableFrom(type)) {
                    if (((CmsBoolean) f.get(this)).value()) packed |= (1 << pos);
                } else if (type == int.class || type == Integer.class) {
                    int val = f.getInt(this);
                    packed |= (val & mask(len)) << pos;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync to inner", e);
            }
        }
        writePacked(packed);
    }
}
