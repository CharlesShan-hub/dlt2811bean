package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Release-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.2.2
 *
 * NOTE: In ASN.1, Release-ErrorPDU is defined as ServiceError,
 * but C side prepends reqId for all PDUs.
 */
public class CmsReleaseError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsReleaseError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsReleaseError reqId(int v) { this.reqId.value(v); return this; }
    public CmsReleaseError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeReleaseError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeReleaseError(nativePtr, data); read(); }
}