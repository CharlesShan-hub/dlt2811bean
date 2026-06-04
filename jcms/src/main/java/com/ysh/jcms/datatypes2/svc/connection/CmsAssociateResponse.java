package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;
import com.ysh.jcms.datatypes2.data.basic.CmsBoolean;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_associate_response_t — Associate-ResponsePDU.
 */
public class CmsAssociateResponse extends CmsStructure {
    public CmsAssociationId assocId = new CmsAssociationId();
    public int serviceError;
    public CmsBoolean hasAuth = new CmsBoolean();
    public byte[] cert = new byte[2048];
    public int certLen;
    public long signedTime;
    public byte[] sigVal = new byte[2048];
    public int sigLen;

    @Override protected List<String> getFieldOrder() { return Arrays.asList("assocId", "serviceError", "hasAuth", "cert", "certLen", "signedTime", "sigVal", "sigLen"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_associate_response_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_associate_response_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsAssociateResponse from(byte[] data) { return new CmsAssociateResponse().decode(data); }
}
