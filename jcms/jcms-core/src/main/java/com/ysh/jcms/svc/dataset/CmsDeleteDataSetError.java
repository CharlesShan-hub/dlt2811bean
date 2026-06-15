package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * DeleteDataSet-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.5.4
 */
public class CmsDeleteDataSetError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsDeleteDataSetError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeDeleteDataSetError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeDeleteDataSetError(nativePtr, data); read(); }
}
