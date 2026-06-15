package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetURCBValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.7.4
 */
public class CmsGetUrcbValuesError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetUrcbValuesError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetUrcbValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetUrcbValuesError(nativePtr, data); read(); }
}
