package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectActiveSG-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.6.1
 */
public class CmsSelectActiveSgError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsSelectActiveSgError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeSelectActiveSgError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeSelectActiveSgError(nativePtr, data); read(); }
}
