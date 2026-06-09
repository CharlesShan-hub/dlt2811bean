package com.ysh.jcms2.data.scalar;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * typedef struct { uint8_t value[4]; } cms_float32_t;
 * sizeof = 4
 * PER: 4 bytes aligned
 */
public class CmsFloat32 extends CmsType {

    private float value;

    public CmsFloat32() {}
    public CmsFloat32(float value) { this.value = value; write(); }

    public float value() { return value; }
    public CmsFloat32 value(float v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 4; }
    @Override public void write() { nativePtr.setFloat(0, value); }
    @Override public void read() { this.value = nativePtr.getFloat(0); }
    @Override public byte[] encode() { write(); return NativeBridge.encodeFloat32(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeFloat32(nativePtr, data); read(); }
}
