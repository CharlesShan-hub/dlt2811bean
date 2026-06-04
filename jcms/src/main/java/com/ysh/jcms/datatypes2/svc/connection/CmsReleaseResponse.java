package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_release_response_t — Release-ResponsePDU.
 */
public class CmsReleaseResponse extends CmsStructure {
    public CmsAssociationId assocId = new CmsAssociationId();
    public int serviceError;

    @Override protected List<String> getFieldOrder() { return Arrays.asList("assocId", "serviceError"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_release_response_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_release_response_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 128; }
    public static CmsReleaseResponse from(byte[] data) { return new CmsReleaseResponse().decode(data); }
}
