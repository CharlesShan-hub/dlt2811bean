package com.ysh.jcms2;

/**
 * typedef struct { int32_t value; } cms_service_error_t;
 * sizeof = 4
 * 叶子类型 — 没有 children。
 */
public class CmsServiceError extends CmsType {

    public static final int NO_ERROR                          = 0;
    public static final int INSTANCE_NOT_AVAILABLE            = 1;
    public static final int INSTANCE_IN_USE                   = 2;
    // ... 其余常量见 ASN.1

    private int value;

    public CmsServiceError() {}
    public CmsServiceError(int value) { this.value = value; write(); }

    public int value() { return value; }
    public CmsServiceError value(int v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 4; }
    @Override public void write() { nativePtr.setInt(0, value); }
    @Override public void read() { this.value = nativePtr.getInt(0); }
}
