package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_release_request_t — Release-RequestPDU.
 */
public class CmsReleaseRequest extends CmsStructure {
    public CmsAssociationId assocId = new CmsAssociationId();

    @Override protected List<String> getFieldOrder() { return Arrays.asList("assocId"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_release_request_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_release_request_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 128; }
    public static CmsReleaseRequest from(byte[] data) { return new CmsReleaseRequest().decode(data); }
}
