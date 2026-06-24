package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { int64_t value; } cms_int64_t;
 * sizeof = 8
 */
public class CmsInt64 extends CmsType {

    private long value = 0;

    public CmsInt64() { super(Codec.INT64);}
    public CmsInt64(long value) { super(Codec.INT64); this.value = value; write(); }

    public long value() { return value; }
    public CmsInt64 value(long v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 8; }
    @Override public void write() { nativePtr.setLong(0, value); }
    @Override public void read() { this.value = nativePtr.getLong(0); }
}
