package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsBitStringFixed;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.basic.CmsVisibleString;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_lcb_t — LCB (Log Control Block).
 */
public class CmsLcb extends CmsStructure {
    public CmsBoolean logEna = new CmsBoolean();
    public CmsVisibleString datSet = new CmsVisibleString(256);
    public CmsBitStringFixed trgOps = new CmsBitStringFixed(6);
    public CmsInt32U intgPd = new CmsInt32U();
    public CmsVisibleString logRef = new CmsVisibleString(256);
    public CmsBitStringFixed optFlds = new CmsBitStringFixed(1);
    public CmsBoolean optFldsPresent = new CmsBoolean();
    public CmsInt32U bufTm = new CmsInt32U();
    public CmsBoolean bufTmPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("logEna", "datSet", "trgOps", "intgPd", "logRef",
                "optFlds", "optFldsPresent", "bufTm", "bufTmPresent");
    }

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_lcb_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_lcb_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 512; }
    public static CmsLcb from(byte[] data) { return new CmsLcb().decode(data); }
}
