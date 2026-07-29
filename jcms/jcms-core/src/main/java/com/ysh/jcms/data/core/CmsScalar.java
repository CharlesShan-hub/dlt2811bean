package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;

/**
 * Base for single-value types whose Inner* stores the value in
 * a public field named {@code value}.
 *
 * <p>{@link #innerGet()} reads directly from {@code inner.value};
 * {@link #innerSet(Object)} immediately pushes the value into
 * the Inner* tree.
 *
 * <p>The no-arg constructor is for container types whose Inner*
 * does not have a {@code value} field (the {@code valueField}
 * reflection handle will be {@code null}).
 */
public abstract class CmsScalar extends CmsType {

    // ── 反射缓存 ──────────────────────────────────────────────────────

    /** Inner*.value 字段的反射句柄。 */
    private final Field valueField;
    /** 当 valueField 为 null 时的兜底存储（如 directWrappers 场景）。 */
    private Object directValue;

    // ── 构造器 ─────────────────────────────────────────────────────────

    protected CmsScalar() {
        this.valueField = null;
    }

    protected CmsScalar(InnerBase inner) {
        super(inner);
        this.valueField = findValueField(inner);
        if (valueField != null) {
            try {
                Object val = valueField.get(inner);
                if (val instanceof InnerBase) {
                    Field innerValF = val.getClass().getField("value");
                    this.directValue = innerValF.get(val);
                } else {
                    this.directValue = val;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Field findValueField(InnerBase inner) {
        try {
            return inner.getClass().getField("value");
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    // ── 读写 value ───────────────────────────────────────────────────

    /** 从 inner.value 或 directValue 读取。 */
    protected Object innerGet() {
        if (valueField != null) {
            try {
                Object val = valueField.get(inner);
                if (val instanceof InnerBase) {
                    Field innerValF = val.getClass().getField("value");
                    return innerValF.get(val);
                }
                return val;
            } catch (Exception e) {
                return null;
            }
        }
        return directValue;
    }

    /** 写入 directValue 并立即同步到 inner.value（存在 valueField 时）。 */
    protected void innerSet(Object v) {
        this.directValue = v;
        if (valueField == null) return;
        try {
            Object val = valueField.get(inner);
            if (val instanceof InnerBase) {
                Field innerValF = val.getClass().getField("value");
                innerValF.set(val, v);
            } else if (val == null && InnerBase.class.isAssignableFrom(valueField.getType())) {
                Object wrapper = valueField.getType().getDeclaredConstructor().newInstance();
                Field innerValF = wrapper.getClass().getField("value");
                innerValF.set(wrapper, v);
                valueField.set(inner, wrapper);
            } else {
                valueField.set(inner, v);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void syncFromInner() {
        if (valueField != null) {
            try {
                Object val = valueField.get(inner);
                if (val instanceof InnerBase) {
                    Field innerValF = val.getClass().getField("value");
                    this.directValue = innerValF.get(val);
                } else {
                    this.directValue = val;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void syncToInner() {
        // innerSet() 已即时同步，无需操作
        super.syncToInner();
    }
}
