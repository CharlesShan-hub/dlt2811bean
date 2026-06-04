package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT32 — signed 32-bit integer.
 */
public class CmsInt32 extends CmsIntegerType<CmsInt32> {
    public static final int SIZE = 4;
    public CmsInt32() { this(0); }
    public CmsInt32(int value) { super(SIZE, value, false); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int32_encode(intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int32_decode(data, data.length, value);
    }

    public static CmsInt32 from(byte[] data) { return new CmsInt32().decode(data); }
}
