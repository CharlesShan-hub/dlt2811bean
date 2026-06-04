package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsBitStringFixed;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.basic.CmsOctetString;
import com.ysh.jcms.datatypes2.data.basic.CmsVisibleString;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_urcb_t — URCB (Unbuffered Report Control Block).
 */
public class CmsUrcb extends CmsStructure {
    public CmsVisibleString rptID = new CmsVisibleString(130);
    public CmsBoolean rptEna = new CmsBoolean();
    public CmsVisibleString datSet = new CmsVisibleString(256);
    public CmsInt32U confRev = new CmsInt32U();
    public CmsBitStringFixed optFlds = new CmsBitStringFixed(10);
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsInt16U sqNum = new CmsInt16U();
    public CmsBitStringFixed trgOps = new CmsBitStringFixed(6);
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsBoolean resv = new CmsBoolean();
    public CmsOctetString owner = new CmsOctetString(64);
    public CmsInt32U ownerLen = new CmsInt32U();
    public CmsBoolean ownerPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("rptID", "rptEna", "datSet", "confRev", "optFlds", "bufTm",
                "sqNum", "trgOps", "intgPd", "gi", "resv", "owner", "ownerLen", "ownerPresent");
    }

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_urcb_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_urcb_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 512; }
    public static CmsUrcb from(byte[] data) { return new CmsUrcb().decode(data); }
}
