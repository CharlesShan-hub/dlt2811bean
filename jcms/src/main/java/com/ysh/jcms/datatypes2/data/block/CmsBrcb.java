package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsBitStringFixed;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.basic.CmsOctetStringFixed;
import com.ysh.jcms.datatypes2.data.basic.CmsVisibleStringFixed;
import com.ysh.jcms.datatypes2.data.extended.CmsBinaryTime;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_brcb_t — BRCB (Buffered Report Control Block).
 */
public class CmsBrcb extends CmsStructure {
    public CmsVisibleStringFixed rptID = new CmsVisibleStringFixed(130);
    public CmsBoolean rptEna = new CmsBoolean();
    public CmsVisibleStringFixed datSet = new CmsVisibleStringFixed(256);
    public CmsInt32U confRev = new CmsInt32U();
    public CmsBitStringFixed optFlds = new CmsBitStringFixed(10);
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsInt16U sqNum = new CmsInt16U();
    public CmsBitStringFixed trgOps = new CmsBitStringFixed(6);
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsBoolean gi = new CmsBoolean();
    public CmsBoolean purgeBuf = new CmsBoolean();
    public CmsOctetStringFixed entryID = new CmsOctetStringFixed(8);
    public CmsBinaryTime timeOfEntry = new CmsBinaryTime();
    public CmsInt16 resvTms = new CmsInt16();
    public CmsBoolean resvTmsPresent = new CmsBoolean();
    public CmsOctetStringFixed owner = new CmsOctetStringFixed(64);
    public CmsInt32U ownerLen = new CmsInt32U();
    public CmsBoolean ownerPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() { return Arrays.asList("rptID", "rptEna", "datSet", "confRev", "optFlds", "bufTm",
            "sqNum", "trgOps", "intgPd", "gi", "purgeBuf", "entryID", "timeOfEntry",
            "resvTms", "resvTmsPresent", "owner", "ownerLen", "ownerPresent"); }

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_brcb_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_brcb_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 512; }
    public static CmsBrcb from(byte[] data) { return new CmsBrcb().decode(data); }
}
