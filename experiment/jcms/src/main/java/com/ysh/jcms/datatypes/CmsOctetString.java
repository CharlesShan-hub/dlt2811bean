package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsOctetString {

    private final byte[] value;

    public CmsOctetString(byte[] value) {
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_OctetString(value, value.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsOctetString decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_OctetString(data, data.length, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsOctetString(result);
    }
}
