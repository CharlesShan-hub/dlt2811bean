package com.ysh.jcms.datatypes2.data.extended;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt8U;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_utc_time_t — UtcTime (SEQUENCE of Int32U + Int24U + TimeQuality)
 */
public class CmsUtcTime extends CmsStructure {
    public CmsInt32U seconds_since_epoch = new CmsInt32U();
    public CmsInt32U fraction_of_second = new CmsInt32U();
    public CmsInt8U time_quality = new CmsInt8U();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_utc_time_encode(this, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFI.INSTANCE.cms_utc_time_decode(data, data.length, this);
    }

    @Override
    protected int encodeBufSize() { return 16; }

    public static CmsUtcTime from(byte[] data) { return new CmsUtcTime().decode(data); }
}
