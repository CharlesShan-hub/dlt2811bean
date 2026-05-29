package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsMsvcbOptFlds {

    private final byte[] value;

    public CmsMsvcbOptFlds(byte[] value) {
        if (value.length != 1) {
            throw new IllegalArgumentException("MsvcbOptFlds must be exactly 1 byte (8 bits)");
        }
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_MsvcbOptFlds(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsMsvcbOptFlds decode(byte[] data) {
        byte[] val = new byte[1];
        CmsFFI.INSTANCE.cms_ffi_decode_MsvcbOptFlds(data, data.length, val);
        return new CmsMsvcbOptFlds(val);
    }
}
