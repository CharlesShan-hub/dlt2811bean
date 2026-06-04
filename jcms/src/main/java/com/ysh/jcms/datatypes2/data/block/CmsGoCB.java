package com.ysh.jcms.datatypes2.data.block;

import com.sun.jna.ptr.IntByReference;
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
 * cms_gocb_t — GoCB (GOOSE Control Block).
 */
public class CmsGoCB extends CmsStructure {
    public CmsBoolean goEna = new CmsBoolean();
    public CmsVisibleString goID = new CmsVisibleString(130);
    public CmsVisibleString datSet = new CmsVisibleString(256);
    public CmsInt32U confRev = new CmsInt32U();
    public CmsBoolean ndsCom = new CmsBoolean();
    public byte[] dstAddr = new byte[6];
    public CmsInt8U dstPriority = new CmsInt8U();
    public CmsInt16U dstVid = new CmsInt16U();
    public CmsInt16U dstAppId = new CmsInt16U();
    public CmsBoolean dstAddressPresent = new CmsBoolean();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("goEna", "goID", "datSet", "confRev", "ndsCom",
                "dstAddr", "dstPriority", "dstVid", "dstAppId", "dstAddressPresent");
    }

    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_gocb_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_gocb_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 512; }
    public static CmsGoCB from(byte[] data) { return new CmsGoCB().decode(data); }
}
