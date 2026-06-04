package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

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
    protected List<String> getFieldOrder() {
        return Arrays.asList(); // flat FFI — no struct fields
    }

    @Override
    public byte[] encode() {
        syncToNative();
        byte[] buf = new byte[encodeBufSize()];
        IntByReference outLen = new IntByReference(buf.length);
        ffiEncode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsPhyComAddr decode(byte[] data) {
        ffiDecode(data);
        syncFromNative();
        return this;
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
