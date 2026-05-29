package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;
import java.util.Arrays;

public class CmsFC extends AbstractCmsScalar<byte[]> {

    public CmsFC() {
        super("FC", new byte[2]);
    }

    public CmsFC(byte[] value) {
        super("FC", new byte[2]);
        set(value);
    }

    @Override
    public void set(byte[] value) {
        if (value.length != 2) {
            throw new IllegalArgumentException("FC must be exactly 2 bytes (16 bits)");
        }
        super.set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_FC(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsFC decode(byte[] data) {
        byte[] val = new byte[2];
        CmsFFIDatatypes.INSTANCE.cms_decode_FC(data, data.length, val);
        return new CmsFC(val);
    }

    @Override
    public CmsFC copy() {
        CmsFC clone = new CmsFC();
        return copyTo(clone);
    }
}
