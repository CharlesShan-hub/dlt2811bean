package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteFile-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.12.3
 */
public class CmsDeleteFileError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsDeleteFileError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    // -- chain setters --
    public CmsDeleteFileError reqId(int v) { this.reqId.value(v); return this; }
    public CmsDeleteFileError serviceError(int v) { this.serviceError.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeDeleteFileError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeDeleteFileError(nativePtr, data); read(); }
}