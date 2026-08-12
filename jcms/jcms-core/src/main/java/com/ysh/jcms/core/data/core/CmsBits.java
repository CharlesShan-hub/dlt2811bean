package com.ysh.jcms.core.data.core;

import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.V;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base class for BIT STRING types.
 *
 * <p>
 * The packed value is stored in {@code inner._v} as a hex string (JER format).
 * Subclasses declare fields annotated with {@link Bit} for individual bit
 * positions.
 */
public abstract class CmsBits extends CmsType {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Bit {
        int value();
        int length() default 1;
    }

    /**
     * Per-class metadata: @Bit fields and total bit width, built once per class.
     */
    private static final ClassValue<BitsMeta> BITS_META = new ClassValue<BitsMeta>() {
        @Override
        protected BitsMeta computeValue(Class<?> type) {
            List<Field> fields = new ArrayList<>();
            int max = 0;
            for (Field f : type.getFields()) {
                Bit bit = f.getAnnotation(Bit.class);
                if (bit == null)
                    continue;
                fields.add(f);
                max = Math.max(max, bit.value() + bit.length());
            }
            return new BitsMeta(fields, max > 0 ? max : 1);
        }
    };

    private static final class BitsMeta {
        final List<Field> bitFields;
        final int bitCount;

        BitsMeta(List<Field> bitFields, int bitCount) {
            this.bitFields = bitFields;
            this.bitCount = bitCount;
        }
    }

    protected CmsBits(InnerBase inner) {
        super(inner);
    }

    /** Total bit width from @Bit annotations (cached per class). */
    private int bitCount() {
        return BITS_META.get(getClass()).bitCount;
    }

    /**
     * Read packed value from _v. Supports hex string or JER form {"value": "HEX",
     * "length": N}.
     */
    private int readPacked() {
        Object v = V.getVal(inner._v);
        if (v instanceof String) {
            return InnerBase.parseBitStringHex((String) v, bitCount());
        }
        if (v instanceof Map) {
            // JER form: {"value": "AABB", "length": 16} (rasn output)
            Object hex = ((Map<?, ?>) v).get("value");
            if (hex instanceof String) {
                return InnerBase.parseBitStringHex((String) hex, bitCount());
            }
        }
        return 0;
    }

    /** Write packed value to _v (int → hex string). */
    private void writePacked(int v) {
        V.setVal(inner._v, InnerBase.bitStringHex(v, bitCount()));
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
        // Copy the @Bit Java fields (they are the source of truth; _v only holds the
        // encoded form). Copying v.value() would lose fields set via fluent setters
        // (e.g. .sequence_number(true)) that were never packed into _v.
        for (Field f : BITS_META.get(getClass()).bitFields) {
            try {
                f.set(this, f.get(v));
            } catch (Exception e) {
                throw new RuntimeException("Failed to copy bit field " + f.getName(), e);
            }
        }
        return this;
    }

    @Override
    public void syncToInner() {
        int packed = 0;
        for (Field f : BITS_META.get(getClass()).bitFields) {
            try {
                Bit bit = f.getAnnotation(Bit.class);
                int pos = bit.value();
                int len = bit.length();
                Class<?> type = f.getType();

                if (len == 1 && (type == boolean.class || type == Boolean.class)) {
                    if (f.getBoolean(this))
                        packed |= (1 << pos);
                } else if (len == 1 && CmsBoolean.class.isAssignableFrom(type)) {
                    if (((CmsBoolean) f.get(this)).value())
                        packed |= (1 << pos);
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

    /** Populate @Bit Java fields from the packed value in {@code _v}. */
    @Override
    public void syncFromInner() {
        int packed = readPacked();
        for (Field f : BITS_META.get(getClass()).bitFields) {
            try {
                Bit bit = f.getAnnotation(Bit.class);
                int pos = bit.value();
                int len = bit.length();
                Class<?> type = f.getType();
                if (len == 1 && (type == boolean.class || type == Boolean.class)) {
                    f.setBoolean(this, (packed & (1 << pos)) != 0);
                } else if (len == 1 && CmsBoolean.class.isAssignableFrom(type)) {
                    ((CmsBoolean) f.get(this)).value((packed & (1 << pos)) != 0);
                } else if (type == int.class || type == Integer.class) {
                    f.setInt(this, (packed >> pos) & mask(len));
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync from inner", e);
            }
        }
    }
}
