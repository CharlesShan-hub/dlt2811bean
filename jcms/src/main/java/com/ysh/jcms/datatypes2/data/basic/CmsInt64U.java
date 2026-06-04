package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT64U — unsigned 64-bit integer.
 */
public class CmsInt64U extends CmsIntegerType<CmsInt64U> {
    public static final int SIZE = 8;
    public CmsInt64U() { this(0L); }
    public CmsInt64U(long value) { super(SIZE, value, true); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int64u_encode(longValue(), buf, outLen);
    }

    @Override
    public CmsInt64U decode(byte[] data) {
        LongByReference val = new LongByReference();
        CmsFFI.INSTANCE.cms_int64u_decode(data, data.length, val);
        setValue(val.getValue());
        return this;
    }

    public static CmsInt64U from(byte[] data) { return new CmsInt64U().decode(data); }
}
