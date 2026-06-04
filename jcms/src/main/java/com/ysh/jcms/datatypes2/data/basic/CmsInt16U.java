package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT16U — unsigned 16-bit integer.
 */
public class CmsInt16U extends CmsIntegerType<CmsInt16U> {
    public static final int SIZE = 2;
    public CmsInt16U() { this(0); }
    public CmsInt16U(int value) { super(SIZE, value, true); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int16u_encode((short) intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int16u_decode(data, data.length, value);
    }

    public static CmsInt16U from(byte[] data) { return new CmsInt16U().decode(data); }
}
