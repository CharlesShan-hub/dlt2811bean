package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsBoolean {

    private final boolean value;

    public CmsBoolean(boolean value) {
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_BOOLEAN(value ? 1 : 0, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBoolean decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_BOOLEAN(data, data.length, v);
        return new CmsBoolean(v.getValue() != 0);
    }
}
