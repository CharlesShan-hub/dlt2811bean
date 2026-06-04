package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsBitStringFixed;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt8U;
import com.ysh.jcms.datatypes2.data.basic.CmsVisibleString;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_msvcb_t — MSVCB (Multicast Sampled Value Control Block).
 */
public class CmsMsvcb extends CmsStructure {
    public CmsBoolean svEna = new CmsBoolean();
    public CmsVisibleString msvID = new CmsVisibleString(130);
    public CmsVisibleString datSet = new CmsVisibleString(256);
    public CmsInt32U confRev = new CmsInt32U();
    public int smpMod;
    public CmsBoolean smpModPresent = new CmsBoolean();
    public CmsInt16U smpRate = new CmsInt16U();
    public CmsBitStringFixed optFlds = new CmsBitStringFixed(5);
    public byte[] dstAddr = new byte[6];
    public CmsInt8U dstPriority = new CmsInt8U();
    public CmsInt16U dstVid = new CmsInt16U();
    public CmsInt16U dstAppId = new CmsInt16U();
    public CmsBoolean dstAddressPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("svEna", "msvID", "datSet", "confRev",
                "smpMod", "smpModPresent", "smpRate", "optFlds",
                "dstAddr", "dstPriority", "dstVid", "dstAppId", "dstAddressPresent");
    }

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_msvcb_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_msvcb_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 512; }
    public static CmsMsvcb from(byte[] data) { return new CmsMsvcb().decode(data); }
}
