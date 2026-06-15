package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * CreateDataSet-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.5.3
 */
public class CmsCreateDataSetError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsCreateDataSetError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    // -- chain setters --
    public CmsCreateDataSetError reqId(int v) { this.reqId.value(v); return this; }
    public CmsCreateDataSetError serviceError(int v) { this.serviceError.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeCreateDataSetError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeCreateDataSetError(nativePtr, data); read(); }
}