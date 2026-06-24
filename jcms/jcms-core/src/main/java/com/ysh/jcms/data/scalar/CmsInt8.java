package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { int8_t value; } cms_int8_t;
 * sizeof = 1
 */
public class CmsInt8 extends CmsType {

    private byte value = 0;

    public CmsInt8() { super(Codec.INT8);}
    public CmsInt8(byte value) { super(Codec.INT8); this.value = value; write(); }
    public CmsInt8(int value)  { this((byte) value); }

    public byte value() { return value; }
    public CmsInt8 value(byte v) { this.value = v; write(); return this; }
    public CmsInt8 value(int v)  { return value((byte) v); }

    @Override
    protected int calcNativeSize() { return 1; }

    @Override
    public void write() { nativePtr.setByte(0, value); }

    @Override
    public void read() { this.value = nativePtr.getByte(0); }
}
