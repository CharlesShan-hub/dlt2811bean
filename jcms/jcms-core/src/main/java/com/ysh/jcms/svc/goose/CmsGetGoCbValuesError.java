package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoCbValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.9.4
 */
public class CmsGetGoCbValuesError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetGoCbValuesError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsGetGoCbValuesError reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetGoCbValuesError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGoCbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGoCbValuesError(nativePtr, data); read(); }
}