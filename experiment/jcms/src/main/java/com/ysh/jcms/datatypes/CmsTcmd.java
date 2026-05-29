package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsTcmd {

    private final int value;

    public CmsTcmd(int value) {
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_Tcmd(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsTcmd decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_Tcmd(data, data.length, v);
        return new CmsTcmd(v.getValue());
    }
}
