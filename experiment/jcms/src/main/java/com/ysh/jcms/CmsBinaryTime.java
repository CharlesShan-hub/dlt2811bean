package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
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
        CmsFFI.INSTANCE.cms_encode_BinaryTime(hour, minute, second, millisecond, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBinaryTime decode(byte[] data) {
        IntByReference h = new IntByReference();
        IntByReference m = new IntByReference();
        IntByReference s = new IntByReference();
        IntByReference ms = new IntByReference();
        CmsFFI.INSTANCE.cms_decode_BinaryTime(data, data.length, h, m, s, ms);
        return new CmsBinaryTime(h.getValue(), m.getValue(), s.getValue(), ms.getValue());
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d.%03d", hour, minute, second, millisecond);
    }
}
