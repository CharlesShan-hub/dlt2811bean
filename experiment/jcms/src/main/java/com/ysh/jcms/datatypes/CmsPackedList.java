package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsPackedList {

    private final byte[] value;

    public CmsPackedList(byte[] value) {
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_PackedList(value, value.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsPackedList decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFI.INSTANCE.cms_ffi_decode_PackedList(data, data.length, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsPackedList(result);
    }
}
