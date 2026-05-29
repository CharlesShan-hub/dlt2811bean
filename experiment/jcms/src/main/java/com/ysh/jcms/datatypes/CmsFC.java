package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsFC {

    private final byte[] value;

    public CmsFC(byte[] value) {
        if (value.length != 2) {
            throw new IllegalArgumentException("FC must be exactly 2 bytes (16 bits)");
        }
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_FC(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFC decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFI.INSTANCE.cms_ffi_decode_FC(data, data.length, val);
        return new CmsFC(val);
    }
}
