package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { uint32_t value; } cms_int32u_t; sizeof = 4 value() 返回 long
 * 以表示 0..4294967295（Java int 装不下全范围）。
 */
public class CmsInt32U extends CmsType {

    private long value = 0L; /* unsigned int32, 用 long 存 */

    public CmsInt32U() {
        super(Codec.INT32U);
    }
    public CmsInt32U(long value) {
        super(Codec.INT32U);
        this.value = value & 0xFFFFFFFFL;
        write();
    }

    public long value() {
        return value;
    }
    public CmsInt32U value(long v) {
        this.value = v & 0xFFFFFFFFL;
        write();
        return this;
    }

    @Override
    protected int calcNativeSize() {
        return 4;
    }
    @Override
    public void write() {
        nativePtr.setInt(0, (int) value);
    }
    @Override
    public void read() {
        this.value = nativePtr.getInt(0) & 0xFFFFFFFFL;
    }
}
