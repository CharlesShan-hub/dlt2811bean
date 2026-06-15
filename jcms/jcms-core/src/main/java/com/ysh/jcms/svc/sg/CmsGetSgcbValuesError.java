package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetSGCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.6.6
 */
public class CmsGetSgcbValuesError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetSgcbValuesError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsGetSgcbValuesError reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetSgcbValuesError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetSgcbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetSgcbValuesError(nativePtr, data); read(); }
}