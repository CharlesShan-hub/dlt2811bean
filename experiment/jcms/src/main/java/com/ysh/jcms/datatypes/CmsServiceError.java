package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsServiceError {

    private final int value;

    public CmsServiceError(int value) {
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_ServiceError(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsServiceError decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_ServiceError(data, data.length, v);
        return new CmsServiceError(v.getValue());
    }
}
