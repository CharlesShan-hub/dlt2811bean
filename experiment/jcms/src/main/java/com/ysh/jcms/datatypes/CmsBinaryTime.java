package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsBinaryTime {

    private final int hour;
    private final int minute;
    private final int second;
    private final int millisecond;

    public CmsBinaryTime(int hour, int minute, int second, int millisecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_BinaryTime(hour, minute, second, millisecond, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBinaryTime decode(byte[] data) {
        IntByReference h = new IntByReference();
        IntByReference min = new IntByReference();
        IntByReference sec = new IntByReference();
        IntByReference ms = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_BinaryTime(data, data.length, h, min, sec, ms);
        return new CmsBinaryTime(h.getValue(), min.getValue(), sec.getValue(), ms.getValue());
    }
}
