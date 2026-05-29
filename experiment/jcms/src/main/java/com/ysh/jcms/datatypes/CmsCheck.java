package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsCheck {

    private final byte[] value;

    public CmsCheck(byte[] value) {
        if (value.length != 2) {
            throw new IllegalArgumentException("Check must be exactly 2 bytes (16 bits)");
        }
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Check(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsCheck decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFI.INSTANCE.cms_ffi_decode_Check(data, data.length, val);
        return new CmsCheck(val);
    }
}
