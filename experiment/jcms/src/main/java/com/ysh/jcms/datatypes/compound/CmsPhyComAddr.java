package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsPhyComAddr extends AbstractCmsCompound {

    private final byte[] value;

    public CmsPhyComAddr(byte[] value) {
        super("PhyComAddr");
        if (value.length != 6) {
            throw new IllegalArgumentException("PhyComAddr must be exactly 6 bytes");
        }
        this.value = value;
    }

    public byte[] getValue() { return value; }

    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_PhyComAddr(value, 0, 0, 0, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsPhyComAddr decode(byte[] data) {
        byte[] val = new byte[6];
        IntByReference priority = new IntByReference();
        IntByReference vid = new IntByReference();
        IntByReference appid = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_decode_PhyComAddr(data, data.length, val, priority, vid, appid);
        return new CmsPhyComAddr(val);
    }

    public CmsPhyComAddr copy() {
        return new CmsPhyComAddr(value.clone());
    }
}
