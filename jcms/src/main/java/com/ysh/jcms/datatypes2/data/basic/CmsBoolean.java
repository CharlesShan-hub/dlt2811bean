package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * BOOLEAN — 对应 C 的 int（4 字节）。
 */
public class CmsBoolean extends CmsIntegerType<CmsBoolean> {
    public CmsBoolean() { this(false); }
    public CmsBoolean(boolean value) { super(4, value ? 1 : 0, true); }
    public boolean get() { return intValue() != 0; }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_boolean_encode(intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_boolean_decode(data, data.length, value);
    }

    public static CmsBoolean from(byte[] data) { return new CmsBoolean().decode(data); }
}
