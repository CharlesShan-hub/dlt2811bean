package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGoReference-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.9.2
 */
public class CmsGetGoReferenceError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetGoReferenceError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetGoReferenceError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetGoReferenceError(nativePtr, data); read(); }
}
