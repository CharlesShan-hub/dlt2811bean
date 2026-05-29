package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class CmsBoolean {

    public static final CmsBoolean TRUE = new CmsBoolean(true);
    public static final CmsBoolean FALSE = new CmsBoolean(false);

    private final boolean value;

    public CmsBoolean(boolean value) {
        this.value = value;
    }

    public boolean get() {
        return value;
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
        IntByReference value = new IntByReference();
        CmsFFI.INSTANCE.cms_ffi_decode_BOOLEAN(data, data.length, value);
        return value.getValue() != 0 ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
