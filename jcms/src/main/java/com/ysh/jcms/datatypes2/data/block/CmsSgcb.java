package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt16U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt8U;
import com.ysh.jcms.datatypes2.data.extended.CmsUtcTime;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_sgcb_t — SGCB (Setting Group Control Block).
 */
public class CmsSgcb extends CmsStructure {
    public CmsInt8U numOfSG = new CmsInt8U();
    public CmsInt8U actSG = new CmsInt8U();
    public CmsInt8U editSG = new CmsInt8U();
    public CmsUtcTime tActEdt = new CmsUtcTime();
    public CmsInt16U resvTms = new CmsInt16U();
    public CmsBoolean resvTmsPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("numOfSG", "actSG", "editSG", "tActEdt",
                "resvTms", "resvTmsPresent");
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_sgcb_encode(this, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFI.INSTANCE.cms_sgcb_decode(data, data.length, this);
    }

    @Override
    protected int encodeBufSize() { return 512; }

    public static CmsSgcb from(byte[] data) { return new CmsSgcb().decode(data); }
}
