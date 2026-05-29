package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsPhyComAddr {

    private final byte[] value;

    public CmsPhyComAddr(byte[] value) {
        if (value.length != 6) {
            throw new IllegalArgumentException("PhyComAddr must be exactly 6 bytes");
        }
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_PhyComAddr(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsPhyComAddr decode(byte[] data) {
        byte[] val = new byte[6];
        CmsFFI.INSTANCE.cms_ffi_decode_PhyComAddr(data, data.length, val);
        return new CmsPhyComAddr(val);
    }
}
