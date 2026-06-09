package com.ysh.jcms2.data.scalar;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * typedef struct { uint8_t value[8]; } cms_float64_t;
 * sizeof = 8
 * PER: 8 bytes aligned
 */
public class CmsFloat64 extends CmsType {

    private double value;

    public CmsFloat64() {}
    public CmsFloat64(double value) { this.value = value; write(); }

    public double value() { return value; }
    public CmsFloat64 value(double v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 8; }
    @Override public void write() { nativePtr.setDouble(0, value); }
    @Override public void read() { this.value = nativePtr.getDouble(0); }
    @Override public byte[] encode() { write(); return NativeBridge.encodeFloat64(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeFloat64(nativePtr, data); read(); }
}
