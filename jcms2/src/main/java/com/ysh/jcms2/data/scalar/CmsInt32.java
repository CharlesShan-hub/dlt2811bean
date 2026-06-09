package com.ysh.jcms2.data.scalar;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * typedef struct { int32_t value; } cms_int32_t;
 * sizeof = 4
 */
public class CmsInt32 extends CmsType {

    private int value;

    public CmsInt32() {}
    public CmsInt32(int value) { this.value = value; write(); }

    public int value() { return value; }
    public CmsInt32 value(int v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 4; }
    @Override public void write() { nativePtr.setInt(0, value); }
    @Override public void read() { this.value = nativePtr.getInt(0); }
    @Override public byte[] encode() { write(); return NativeBridge.encodeInt32(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeInt32(nativePtr, data); read(); }
}
