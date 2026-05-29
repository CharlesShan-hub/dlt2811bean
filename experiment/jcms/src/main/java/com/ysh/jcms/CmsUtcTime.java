package com.ysh.jcms;

import com.sun.jna.ptr.LongByReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Getter
@EqualsAndHashCode
public final class CmsUtcTime {

    private final long timestampMs;

    public CmsUtcTime(long timestampMs) {
        this.timestampMs = timestampMs;
    }

    public static CmsUtcTime now() {
        return new CmsUtcTime(System.currentTimeMillis());
    }

    public Date toDate() {
        return new Date(timestampMs);
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        com.sun.jna.ptr.IntByReference outLen = new com.sun.jna.ptr.IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_UtcTime(timestampMs, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsUtcTime decode(byte[] data) {
        LongByReference ts = new LongByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_UtcTime(data, data.length, ts);
        return new CmsUtcTime(ts.getValue());
    }

    @Override
    public String toString() {
        return toDate().toString();
    }
}
