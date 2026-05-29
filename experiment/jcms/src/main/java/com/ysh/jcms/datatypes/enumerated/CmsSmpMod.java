package com.ysh.jcms.datatypes.enumerated;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsSmpMod extends AbstractCmsEnumerated {

    public CmsSmpMod() {
        this(0);
    }

    public CmsSmpMod(int value) {
        super("SmpMod", value, 3);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_SmpMod(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsSmpMod decode(byte[] data) {
        IntByReference v = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_SmpMod(data, data.length, v);
        return new CmsSmpMod(v.getValue());
    }

    @Override
    public CmsSmpMod copy() {
        CmsSmpMod clone = new CmsSmpMod();
        return copyTo(clone);
    }
}
