package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * ConfirmEditSGValues-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.6.4
 */
public class CmsConfirmEditSgValuesError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsConfirmEditSgValuesError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    // -- chain setters --
    public CmsConfirmEditSgValuesError reqId(int v) { this.reqId.value(v); return this; }
    public CmsConfirmEditSgValuesError serviceError(int v) { this.serviceError.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeConfirmEditSgValuesError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeConfirmEditSgValuesError(nativePtr, data); read(); }
}