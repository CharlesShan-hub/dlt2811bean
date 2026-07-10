package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { int32_t value; } cms_int32_t; sizeof = 4
 */
public class CmsInt32 extends CmsType {

    private int value = 0;

    public CmsInt32() {
        super(Codec.INT32);
    }
    public CmsInt32(int value) {
        super(Codec.INT32);
        this.value = value;
        write();
    }

    public int value() {
        return value;
    }
    public CmsInt32 value(int v) {
        this.value = v;
        write();
        return this;
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }
    @Override
    public void write() {
        nativePtr.setInt(0, value);
    }
    @Override
    public void read() {
        this.value = nativePtr.getInt(0);
    }
}
