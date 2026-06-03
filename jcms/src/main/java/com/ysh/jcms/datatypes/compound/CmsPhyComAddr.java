package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsPhyComAddr extends AbstractCmsCompound<CmsPhyComAddr> {

    public byte[] value;

    public CmsPhyComAddr() {
        super("PhyComAddr");
    }

    public CmsPhyComAddr(byte[] value) {
        this();
        if (value.length != 6) {
            throw new IllegalArgumentException("PhyComAddr must be exactly 6 bytes");
        }
        this.value = value;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_phy_com_addr_encode(value, 0, 0, 0, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[6];
        IntByReference priority = new IntByReference();
        IntByReference vid = new IntByReference();
        IntByReference appid = new IntByReference();
        CmsFFIDatatypes.INSTANCE.cms_phy_com_addr_decode(data, data.length, val, priority, vid, appid);
        this.value = val;
    }

    @Override
    protected int encodeBufSize() {
        return 16;
    }

    public static CmsPhyComAddr from(byte[] data) {
        return new CmsPhyComAddr().decode(data);
    }
}
