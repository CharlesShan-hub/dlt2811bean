package com.ysh.jcms.datatypes2.data.extended;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_binary_time_t — BinaryTime (OCTET STRING SIZE(6): Int32U msOfDay + Int16U daysSince1984)
 */
public class CmsBinaryTime extends CmsStructure {
    public CmsInt32U msOfDay = new CmsInt32U();
    public CmsInt16U daysSince1984 = new CmsInt16U();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("msOfDay", "daysSince1984");
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_binary_time_encode(this, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFI.INSTANCE.cms_binary_time_decode(data, data.length, this);
    }

    @Override
    protected int encodeBufSize() { return 16; }

    public static CmsBinaryTime from(byte[] data) { return new CmsBinaryTime().decode(data); }
}
