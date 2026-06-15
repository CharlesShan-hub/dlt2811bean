package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteFile-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U
 * }  —  8.12.3
 */
public class CmsDeleteFileResponse extends CmsType {

    public CmsReqId reqId;

    public CmsDeleteFileResponse() {
        this.reqId = new CmsReqId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeDeleteFileResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeDeleteFileResponse(nativePtr, data); read(); }
}
