package com.ysh.jcms2;

/**
 * typedef struct { uint32_t value; } cms_int32u_t;
 * sizeof = 4
 * 叶子类型 — 没有 children。
 */
public class CmsInt32U extends CmsType {

    private int value;

    public CmsInt32U() {}
    public CmsInt32U(int value) { this.value = value; write(); }

    public int value() { return value; }
    public CmsInt32U value(int v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 4; }
    @Override public void write() { nativePtr.setInt(0, value); }
    @Override public void read() { this.value = nativePtr.getInt(0); }
}
