package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { int16_t value; } cms_int16_t; sizeof = 2
 */
public class CmsInt16 extends CmsType {

    private short value = 0;

    public CmsInt16() {
        super(Codec.INT16);
    }
    public CmsInt16(short value) {
        super(Codec.INT16);
        this.value = value;
        write();
    }
    public CmsInt16(int value) {
        this((short) value);
    }

    public short value() {
        return value;
    }
    public CmsInt16 value(short v) {
        this.value = v;
        write();
        return this;
    }
    public CmsInt16 value(int v) {
        return value((short) v);
    }

    @Override
    protected int calcNativeSize() {
        return 2;
    }
    @Override
    public void write() {
        nativePtr.setShort(0, value);
    }
    @Override
    public void read() {
        this.value = nativePtr.getShort(0);
    }
}
