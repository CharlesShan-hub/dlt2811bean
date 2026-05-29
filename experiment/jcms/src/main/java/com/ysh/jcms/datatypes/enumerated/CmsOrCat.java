package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsOrCat extends AbstractCmsEnumerated {

    public CmsOrCat() {
        this(0);
    }

    public CmsOrCat(int value) {
        super("OrCat", value, 9);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_Originator(value, new byte[0], 0, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsOrCat decode(byte[] data) {
        IntByReference v = new IntByReference();
        byte[] ident = new byte[64];
        IntByReference identCap = new IntByReference(ident.length);
        CmsFFIDatatypes.INSTANCE.cms_decode_Originator(data, data.length, v, ident, identCap);
        return new CmsOrCat(v.getValue());
    }

    @Override
    public CmsOrCat copy() {
        CmsOrCat clone = new CmsOrCat();
        return copyTo(clone);
    }
}
