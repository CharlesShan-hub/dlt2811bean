package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT8U — unsigned 8-bit integer.
 */
public class CmsInt8U extends CmsIntegerType<CmsInt8U> {
    public static final int SIZE = 1;
    public CmsInt8U() { this(0); }
    public CmsInt8U(int value) { super(SIZE, value, true); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int8u_encode((byte) intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int8u_decode(data, data.length, value);
    }

    public static CmsInt8U from(byte[] data) { return new CmsInt8U().decode(data); }
}
