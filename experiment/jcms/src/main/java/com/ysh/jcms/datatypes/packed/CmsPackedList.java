package com.ysh.jcms.datatypes.packed;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsPackedList extends AbstractCmsScalar<byte[]> {

    public CmsPackedList() {
        super("PackedList", new byte[0]);
    }

    public CmsPackedList(byte[] value) {
        super("PackedList", new byte[0]);
        set(value);
    }

    @Override
    public byte[] encode() {
        byte[] buf = new byte[65536];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_PackedList(value, value.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsPackedList decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFI.INSTANCE.cms_decode_PackedList(data, data.length, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsPackedList(result);
    }

    @Override
    public CmsPackedList copy() {
        CmsPackedList clone = new CmsPackedList();
        return copyTo(clone);
    }
}
