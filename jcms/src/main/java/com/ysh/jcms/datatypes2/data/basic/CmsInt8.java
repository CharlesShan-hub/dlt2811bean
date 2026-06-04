package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT8 — signed 8-bit integer.
 */
public class CmsInt8 extends CmsIntegerType<CmsInt8> {
    public static final int SIZE = 1;
    public CmsInt8() { this(0); }
    public CmsInt8(int value) { super(SIZE, value, false); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int8_encode((byte) intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int8_decode(data, data.length, value);
    }

    public static CmsInt8 from(byte[] data) { return new CmsInt8().decode(data); }
}
