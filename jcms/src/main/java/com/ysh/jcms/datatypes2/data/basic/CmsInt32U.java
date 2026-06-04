package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT32U — unsigned 32-bit integer.
 */
public class CmsInt32U extends CmsIntegerType<CmsInt32U> {
    public static final int SIZE = 4;
    public CmsInt32U() { this(0L); }
    public CmsInt32U(long value) { super(SIZE, value, true); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int32u_encode((int) longValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int32u_decode(data, data.length, value);
    }

    public static CmsInt32U from(byte[] data) { return new CmsInt32U().decode(data); }
}
