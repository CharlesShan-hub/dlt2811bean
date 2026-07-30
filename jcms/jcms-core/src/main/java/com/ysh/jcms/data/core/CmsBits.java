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
 * Subclasses declare fields annotated with {@link Bit}.
 */
public abstract class CmsBits extends CmsType {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Bit {
        int value();
        int length() default 1;
    }

    private final Field innerValueField;

    protected CmsBits(InnerBase inner) {
        super(inner);
        this.innerValueField = findInnerValueField(inner);
    }

    private static Field findInnerValueField(InnerBase inner) {
        try {
            return inner.getClass().getField("value");
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private int readPacked() {
        if (innerValueField == null) return 0;
        try {
            Object val = innerValueField.get(inner);
            if (val instanceof Integer) return (Integer) val;
            return innerValueField.getInt(inner);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read packed value", e);
        }
    }

    private void writePacked(int v) {
        if (innerValueField == null) return;
        try {
            if (innerValueField.getType() == Integer.class) {
                innerValueField.set(inner, v);
            } else {
                innerValueField.setInt(inner, v);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to write packed value", e);
        }
    }

    private static int mask(int len) {
        return len >= 32 ? -1 : (1 << len) - 1;
    }

    public int packed() {
        return readPacked();
    }

    public void packed(int v) {
        writePacked(v);
        syncFromInner();
    }

    public void packed(CmsBits v) {
        v.syncToInner();
        packed(v.packed());
    }

    public CmsBits value(CmsBits v) {
        packed(v);
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
                    int v = f.getInt(this);
                    packed |= (v & mask(len)) << pos;
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync to inner", e);
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
                Class<?> type = f.getType();

                if (len == 1 && (type == boolean.class || type == Boolean.class)) {
                    f.setBoolean(this, ((packed >> pos) & 1) != 0);
                } else if (len == 1 && CmsBoolean.class.isAssignableFrom(type)) {
                    ((CmsBoolean) f.get(this)).value(((packed >> pos) & 1) != 0);
                } else if (type == int.class || type == Integer.class) {
                    f.setInt(this, (packed >> pos) & mask(len));
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to sync from inner", e);
            }
        }
    }
}