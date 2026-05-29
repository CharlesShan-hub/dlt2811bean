package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsBinaryTime {

    private final int msOfDay;
    private final int daysSince1984;

    public CmsBinaryTime(int msOfDay, int daysSince1984) {
        this.msOfDay = msOfDay;
        this.daysSince1984 = daysSince1984;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_BinaryTime(msOfDay, daysSince1984, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBinaryTime decode(byte[] data) {
        IntByReference ms = new IntByReference();
        IntByReference days = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_BinaryTime(data, data.length, ms, days);
        return new CmsBinaryTime(ms.getValue(), days.getValue());
    }
}
