package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * Base for single-value types whose Inner* stores the value in
 * a public field named {@code value}.
 *
 * <p>Subclasses read/write {@code innerCache["value"]} only; the
 * sync to/from {@code inner.value} happens automatically:
 * <ul>
 *   <li>{@link #syncToInner()} — before encode, pushes cache → inner
 *   <li>{@link #syncFromInner()} — after decode, pulls inner → cache
 * </ul>
 * So subclasses never need to touch the {@code inner} field directly.
 */
public abstract class CmsScalar extends CmsType {

    private final Field valueField;

    protected CmsScalar() {
        this.valueField = null;
    }

    protected CmsScalar(InnerBase inner) {
        super(inner);
        this.valueField = findValueField(inner);
        syncFromInnerValue();
    }

    private static Field findValueField(InnerBase inner) {
        try {
            return inner.getClass().getField("value");
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    // ── convenience accessors ──────────────────────────────────────

    /** Read the cached value. */
    protected Object innerGet() {
        return innerCache.get("value");
    }

    /** Write the cached value (auto-synced to inner at encode). */
    protected void innerSet(Object v) {
        innerCache.put("value", v);
    }

    // ── automatic sync ─────────────────────────────────────────────

    /** Pull {@code inner.value → innerCache["value"]}. */
    private void syncFromInnerValue() {
        if (valueField == null) return;
        try {
            Object val = valueField.get(inner);
            // DefaultInner* wrappers — unwrap one more level
            if (val instanceof InnerBase) {
                Field innerValF = val.getClass().getField("value");
                innerCache.put("value", innerValF.get(val));
            } else {
                innerCache.put("value", val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Push {@code innerCache["value"] → inner.value}. */
    private void syncToInnerValue() {
        if (valueField == null) return;
        try {
            Object val = valueField.get(inner);
            if (val instanceof InnerBase) {
                // DefaultInner* wrappers — set inner.value.value
                Field innerValF = val.getClass().getField("value");
                innerValF.set(val, innerCache.get("value"));
            } else if (val == null && InnerBase.class.isAssignableFrom(valueField.getType())) {
                // Lazy-init null DefaultInner* inside Inner* (e.g. InnerObjectReference.value)
                Object wrapper = valueField.getType().getDeclaredConstructor().newInstance();
                Field innerValF = wrapper.getClass().getField("value");
                innerValF.set(wrapper, innerCache.get("value"));
                valueField.set(inner, wrapper);
            } else {
                valueField.set(inner, innerCache.get("value"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void syncFromInner() {
        syncFromInnerValue();
    }

    @Override
    public void syncToInner() {
        syncToInnerValue();
        super.syncToInner();
    }

    // ── equals / hashCode ──────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof CmsScalar)) return false;
        Object va = innerCache.get("value");
        Object vb = ((CmsScalar) o).innerCache.get("value");
        if (va instanceof byte[] && vb instanceof byte[]) {
            return Arrays.equals((byte[]) va, (byte[]) vb);
        }
        return Objects.equals(va, vb);
    }

    @Override
    public int hashCode() {
        int h = super.hashCode();
        Object v = innerCache.get("value");
        if (v != null) h = 31 * h + v.hashCode();
        return h;
    }
}
