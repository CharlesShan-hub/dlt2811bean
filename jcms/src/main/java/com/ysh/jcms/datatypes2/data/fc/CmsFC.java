package com.ysh.jcms.datatypes2.data.fc;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsIntegerType;

/**
 * INT8U — FunctionalConstraint (OCTET STRING (SIZE(2))).
 */
public class CmsFC extends CmsIntegerType<CmsFC> {
    public static final int SIZE = 1;
    public CmsFC() { this(0); }
    public CmsFC(int value) { super(SIZE, value, true); }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_int8u_encode((byte) intValue(), buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data, IntByReference value) {
        CmsFFI.INSTANCE.cms_int8u_decode(data, data.length, value);
    }

    public static CmsFC from(byte[] data) { return new CmsFC().decode(data); }
}
