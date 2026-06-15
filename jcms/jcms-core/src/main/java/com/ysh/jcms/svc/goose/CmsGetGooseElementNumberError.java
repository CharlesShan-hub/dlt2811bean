package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGOOSEElementNumber-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.9.3
 */
public class CmsGetGooseElementNumberError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetGooseElementNumberError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    // -- chain setters --
    public CmsGetGooseElementNumberError reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGooseElementNumberError serviceError(int v) { this.serviceError.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGooseElementNumberError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGooseElementNumberError(nativePtr, data); read(); }
}