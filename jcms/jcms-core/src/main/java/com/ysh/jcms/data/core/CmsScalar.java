package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;

/**
 * Base class for scalar types whose Inner* has a public {@code value} field.
 *
 * <p>Supports two storage modes:
 * <ul>
 *   <li>Direct: {@code inner.value} is the value itself</li>
 *   <li>Nested: {@code inner.value} is an InnerBase that contains its own {@code value}</li>
 * </ul>
 *
 * <p>No-arg constructor is for container types without a {@code value} field.
 */
public abstract class CmsScalar extends CmsType {

    /** Reflection handle for Inner*.value, or null if absent. */
    private final Field valueField;

    /** Cached value. Acts as fallback storage when valueField is null. */
    private Object directValue;

    protected CmsScalar() {
        this(new InnerEmpty());
    }

    protected CmsScalar(InnerBase inner) {
        super(inner);
        try {
            this.valueField = inner.getClass().getField("value");
            this.directValue = ValueAccessor.read(inner, valueField);
        } catch (NoSuchFieldException e) {
            this.valueField = null;
        }
    }

    /** Reads from inner.value, or returns directValue if no value field. */
    protected Object innerGet() {
        if (valueField != null) {
            return ValueAccessor.read(inner, valueField);
        }
        return directValue;
    }

    /** Writes to inner.value and updates directValue. */
    protected void innerSet(Object v) {
        this.directValue = v;
        if (valueField != null) {
            ValueAccessor.write(inner, valueField, v);
        }
    }

    @Override
    public void syncFromInner() {
        if (valueField != null) {
            this.directValue = ValueAccessor.read(inner, valueField);
        }
    }

    @Override
    public void syncToInner() {
        // innerSet() writes immediately, so nothing to do here.
    }

    // ── Helper ──────────────────────────────────────────────────────────

    private static class ValueAccessor {
        static Object read(InnerBase inner, Field valueField) {
            try {
                Object val = valueField.get(inner);
                if (val instanceof InnerBase) {
                    return ((InnerBase) val).getClass().getField("value").get(val);
                }
                return val;
            } catch (Exception e) {
                return null;
            }
        }

        static void write(InnerBase inner, Field valueField, Object v) {
            try {
                Object val = valueField.get(inner);
                if (val instanceof InnerBase) {
                    ((InnerBase) val).getClass().getField("value").set(val, v);
                } else if (val == null && InnerBase.class.isAssignableFrom(valueField.getType())) {
                    Object wrapper = valueField.getType().getDeclaredConstructor().newInstance();
                    wrapper.getClass().getField("value").set(wrapper, v);
                    valueField.set(inner, wrapper);
                } else {
                    valueField.set(inner, v);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}