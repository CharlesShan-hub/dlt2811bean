package com.ysh.jcms.datatypes.packed;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsPackedList extends AbstractCmsScalar<CmsPackedList, byte[]> {

    public CmsPackedList() {
        super("PackedList", new byte[0]);
    }

    public CmsPackedList(byte[] value) {
        super("PackedList", new byte[0]);
        set(value);
    }

    @Override
    protected int encodeBufSize() {
        return 65536;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_packed_list_encode(value, value.length, 65535, buf, outLen);
    }

    public static CmsPackedList decode(byte[] data) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_packed_list_decode(data, data.length, 65535, valBuf, valLen);
        byte[] result = new byte[valLen.getValue()];
        System.arraycopy(valBuf, 0, result, 0, result.length);
        return new CmsPackedList(result);
    }
}
