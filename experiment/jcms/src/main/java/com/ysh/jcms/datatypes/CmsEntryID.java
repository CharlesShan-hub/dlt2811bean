package com.ysh.jcms.datatypes;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import lombok.Data;

@Data
public final class CmsEntryID {

    private final byte[] value;

    public CmsEntryID(byte[] value) {
        if (value.length != 8) {
            throw new IllegalArgumentException("EntryID must be exactly 8 bytes");
        }
        this.value = value;
    }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_ffi_encode_EntryID(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsEntryID decode(byte[] data) {
        byte[] val = new byte[8];
        CmsFFI.INSTANCE.cms_ffi_decode_EntryID(data, data.length, val);
        return new CmsEntryID(val);
    }
}
