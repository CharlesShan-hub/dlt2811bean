package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;

/**
 * typedef struct { uint32_t value; } cms_int24u_t;
 * sizeof = 4 (stored in uint32_t, value 0..16777215)
 */
public class CmsInt24U extends CmsType {

    public static final int MAX = 16777215;

    private int value;

    public CmsInt24U() {}
    public CmsInt24U(int value) { this.value = value & MAX; write(); }

    public int value() { return value; }
    public CmsInt24U value(int v) { this.value = v & MAX; write(); return this; }

    @Override protected int calcNativeSize() { return 4; }
    @Override public void write() { nativePtr.setInt(0, value); }
    @Override public void read() { this.value = nativePtr.getInt(0); }
    @Override public byte[] encode() { write(); return NativeBridge.encodeInt24U(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeInt24U(nativePtr, data); read(); }
}
