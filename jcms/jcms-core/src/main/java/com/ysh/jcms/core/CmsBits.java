package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Base class for BIT STRING types whose Inner* stores the packed value
 * in a public {@code value} field (int/Integer).
 *
 * <p>Subclasses declare fields annotated with {@link Bit}:
 * <ul>
 *   <li>{@code boolean} fields → single bit (default length=1)
 *   <li>{@code int} fields → multi-bit field ({@code length} bits)
 * </ul>
 *
 * <p>{@code syncToInner()} and {@code syncFromInner()} are automatic.
 *
 * <pre>{@code
 * public class CmsQuality extends CmsBits {
 *     @Bit(value = 0, length = 2) public int validity;    // 2 bits
 *     @Bit(2) public boolean overflow;                   // 1 bit
 *     // ...
 * }
 * }</pre>
 */
public abstract class CmsBits extends CmsType {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Bit {
        /** Starting bit position (0-based). */
        int value();
        /** Number of bits (1 for boolean, >1 for int fields). */
        int length() default 1;
    }

    private final Field innerValueField;

    protected CmsBits(InnerBase inner) {
        super(inner);
        Field vf;
        try { vf = inner.getClass().getField("value"); }
        catch (NoSuchFieldException e) { vf = null; }
        this.innerValueField = vf;
    }

    private int readPacked() {
        try {
            return innerValueField != null ? innerValueField.getInt(inner) : 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writePacked(int v) {
        try {
            if (innerValueField != null) innerValueField.setInt(inner, v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int mask(int len) {
        return len >= 32 ? -1 : (1 << len) - 1;
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
                Class<?> t = f.getType();
                if (len == 1 && (t == boolean.class || t == Boolean.class)) {
                    if (f.getBoolean(this)) packed |= (1 << pos);
                } else if (len == 1 && CmsBoolean.class.isAssignableFrom(t)) {
                    if (((CmsBoolean) f.get(this)).value()) packed |= (1 << pos);
                } else if (t == int.class || t == Integer.class) {
                    int v = f.getInt(this);
                    packed |= (v & mask(len)) << pos;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        writePacked(packed);
        super.syncToInner();
    }

    @Override
    public void syncFromInner() {
        super.syncFromInner();
        int packed = readPacked();
        for (Field f : getClass().getFields()) {
            Bit bit = f.getAnnotation(Bit.class);
            if (bit == null) continue;
            try {
                int pos = bit.value();
                int len = bit.length();
                Class<?> t = f.getType();
                if (len == 1 && (t == boolean.class || t == Boolean.class)) {
                    f.setBoolean(this, (packed >> pos & 1) != 0);
                } else if (len == 1 && CmsBoolean.class.isAssignableFrom(t)) {
                    ((CmsBoolean) f.get(this)).value((packed >> pos & 1) != 0);
                } else if (t == int.class || t == Integer.class) {
                    f.setInt(this, (packed >> pos) & mask(len));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
