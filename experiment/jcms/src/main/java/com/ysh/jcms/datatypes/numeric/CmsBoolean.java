package com.ysh.jcms.datatypes.numeric;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsBoolean extends AbstractCmsScalar<Boolean> {

    public static final CmsBoolean TRUE = new CmsBoolean(true);
    public static final CmsBoolean FALSE = new CmsBoolean(false);

    public CmsBoolean() {
        super("BOOLEAN", false);
    }

    public CmsBoolean(boolean value) {
        super("BOOLEAN", false);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_boolean_encode(value ? 1 : 0, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsBoolean decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_boolean_decode(data, data.length, v);
        return v.getValue() != 0 ? TRUE : FALSE;
    }

    @Override
    public CmsBoolean copy() {
        CmsBoolean clone = new CmsBoolean();
        return copyTo(clone);
    }
}
