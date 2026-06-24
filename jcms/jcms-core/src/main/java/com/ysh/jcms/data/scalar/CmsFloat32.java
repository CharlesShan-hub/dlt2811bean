package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { uint8_t value[4]; } cms_float32_t;
 * sizeof = 4
 * PER: 4 bytes aligned
 */
public class CmsFloat32 extends CmsType {

    private float value = 0.0f;

    public CmsFloat32() { super(Codec.FLOAT32);}
    public CmsFloat32(float value) { super(Codec.FLOAT32); this.value = value; write(); }

    public float value() { return value; }
    public CmsFloat32 value(float v) { this.value = v; write(); return this; }

    @Override protected int calcNativeSize() { return 4; }
    @Override public void write() { nativePtr.setFloat(0, value); }
    @Override public void read() { this.value = nativePtr.getFloat(0); }
}
