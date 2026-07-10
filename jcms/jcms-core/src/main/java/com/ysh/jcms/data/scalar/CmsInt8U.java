package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { uint8_t value; } cms_int8u_t; sizeof = 1 Java byte 存 C
 * uint8_t，value() 返回 int 0..255。
 */
public class CmsInt8U extends CmsType {

    private byte value = 0;

    public CmsInt8U() {
        super(Codec.INT8U);
    }
    public CmsInt8U(int value) {
        super(Codec.INT8U);
        this.value = (byte) (value & 0xFF);
        write();
    }

    public int value() {
        return value & 0xFF;
    }
    public CmsInt8U value(int v) {
        this.value = (byte) (v & 0xFF);
        write();
        return this;
    }

    @Override
    protected int calcNativeSize() {
        return 1;
    }

    @Override
    public void write() {
        nativePtr.setByte(0, value);
    }

    @Override
    public void read() {
        this.value = nativePtr.getByte(0);
    }
}
