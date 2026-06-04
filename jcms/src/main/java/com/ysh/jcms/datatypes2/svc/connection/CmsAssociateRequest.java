package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.basic.CmsInt64;
import com.ysh.jcms.datatypes2.data.basic.CmsVisibleString;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_associate_request_t — Associate-RequestPDU.
 */
public class CmsAssociateRequest extends CmsStructure {
    public CmsVisibleString sapRef = new CmsVisibleString(65);
    public CmsBoolean hasAuth = new CmsBoolean();
    public byte[] cert = new byte[2048];
    public CmsInt32U certLen = new CmsInt32U();
    public CmsInt64 signedTime = new CmsInt64();
    public byte[] sigVal = new byte[2048];
    public CmsInt32U sigLen = new CmsInt32U();

    @Override protected List<String> getFieldOrder() { return Arrays.asList("sapRef", "hasAuth", "cert", "certLen", "signedTime", "sigVal", "sigLen"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_associate_request_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_associate_request_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsAssociateRequest from(byte[] data) { return new CmsAssociateRequest().decode(data); }
}
