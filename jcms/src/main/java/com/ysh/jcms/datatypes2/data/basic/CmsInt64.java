package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT64 — signed 64-bit integer.
 */
public class CmsInt64 extends CmsIntegerType<CmsInt64> {
    public static final int SIZE = 8;
    public CmsInt64() { this(0L); }
    public CmsInt64(long value) { super(SIZE, value, false); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int64_encode(longValue(), buf, outLen);
    }

    @Override
    public CmsInt64 decode(byte[] data) {
        LongByReference val = new LongByReference();
        CmsFFI.INSTANCE.cms_int64_decode(data, data.length, val);
        setValue(val.getValue());
        return this;
    }

    public static CmsInt64 from(byte[] data) { return new CmsInt64().decode(data); }
}
