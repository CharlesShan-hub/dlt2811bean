package com.ysh.jcms.data.core;

import com.ysh.jcms.data.InnerBase;
import java.lang.reflect.Field;

/**
 * Base for single-value types whose Inner* stores the value in
 * a public field named {@code value}.
 *
 * <p>Subclasses read/write {@code innerCache["value"]} only;
 * {@link #innerSet(Object)} immediately pushes the value into
 * the Inner* tree, so {@code innerCache["value"]} and
 * {@code inner.value} are always in sync.
 *
 * <p>The no-arg constructor is for container types whose Inner*
 * does not have a {@code value} field (the {@code valueField}
 * reflection handle will be {@code null}).
 */
public abstract class CmsScalar extends CmsType {

    // ── 反射缓存 ──────────────────────────────────────────────────────

    /** Inner*.value 字段的反射句柄。 */
    private final Field valueField;

    // ── 构造器 ─────────────────────────────────────────────────────────

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

    // ── 读写 innerCache["value"] ─────────────────────────────────────

    /** 读取 innerCache["value"]。 */
    protected Object innerGet() {
        return innerCache.get("value");
    }

    /** 写入 innerCache["value"] 并立即同步到 inner.value。 */
    protected void innerSet(Object v) {
        innerCache.put("value", v);
        syncToInnerValue();
    }

    // ── inner ↔ innerCache 同步 ──────────────────────────────────────

    /** 从 inner.value 拉取到 innerCache["value"]。 */
    private void syncFromInnerValue() {
        if (valueField == null) return;
        try {
            Object val = valueField.get(inner);
            if (val instanceof InnerBase) {
                // DefaultInner* 包装，再解一层
                Field innerValF = val.getClass().getField("value");
                innerCache.put("value", innerValF.get(val));
            } else {
                innerCache.put("value", val);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 从 innerCache["value"] 推送到 inner.value。
     *  处理 DefaultInner* 嵌套和 null 懒初始化。 */
    private void syncToInnerValue() {
        if (valueField == null) return;
        try {
            Object val = valueField.get(inner);
            if (val instanceof InnerBase) {
                Field innerValF = val.getClass().getField("value");
                innerValF.set(val, innerCache.get("value"));
            } else if (val == null && InnerBase.class.isAssignableFrom(valueField.getType())) {
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
        // innerSet() 已即时同步，无需操作
        super.syncToInner();
    }
}
