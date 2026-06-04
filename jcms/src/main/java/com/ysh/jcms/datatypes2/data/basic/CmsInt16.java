package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT16 — signed 16-bit integer.
 */
public class CmsInt16 extends CmsIntegerType<CmsInt16> {
    public static final int SIZE = 2;
    public CmsInt16() { this(0); }
    public CmsInt16(int value) { super(SIZE, value, false); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int16_encode((short) intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int16_decode(data, data.length, value);
    }

    public static CmsInt16 from(byte[] data) { return new CmsInt16().decode(data); }
}
