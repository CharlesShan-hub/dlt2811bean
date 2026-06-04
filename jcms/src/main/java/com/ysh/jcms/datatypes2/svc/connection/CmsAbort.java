package com.ysh.jcms.datatypes2.svc.connection;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;
import com.ysh.jcms.datatypes2.svc.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;

/**
 * cms_abort_t — AbortPDU.
 */
public class CmsAbort extends CmsStructure {
    public CmsAssociationId assocId = new CmsAssociationId();
    public int reason;

    @Override protected List<String> getFieldOrder() { return Arrays.asList("assocId", "reason"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_abort_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_abort_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 128; }
    public static CmsAbort from(byte[] data) { return new CmsAbort().decode(data); }
}
