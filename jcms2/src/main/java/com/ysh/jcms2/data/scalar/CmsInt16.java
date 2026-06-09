package com.ysh.jcms2.data.scalar;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * typedef struct { int16_t value; } cms_int16_t;
 * sizeof = 2
 */
public class CmsInt16 extends CmsType {

    private short value;

    public CmsInt16() {}
    public CmsInt16(short value) { this.value = value; write(); }
    public CmsInt16(int value)  { this((short) value); }

    public short value() { return value; }
    public CmsInt16 value(short v) { this.value = v; write(); return this; }
    public CmsInt16 value(int v)  { return value((short) v); }

    @Override protected int calcNativeSize() { return 2; }
    @Override public void write() { nativePtr.setShort(0, value); }
    @Override public void read() { this.value = nativePtr.getShort(0); }
    @Override public byte[] encode() { write(); return NativeBridge.encodeInt16(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeInt16(nativePtr, data); read(); }
}
