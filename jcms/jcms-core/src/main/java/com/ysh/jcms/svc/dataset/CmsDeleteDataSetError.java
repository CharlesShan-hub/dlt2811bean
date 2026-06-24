package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsDeleteDataSetError() { super(Codec.DELETE_DATA_SET_ERROR);
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsDeleteDataSetError reqId(int v) { this.reqId.value(v); return this; }
    public CmsDeleteDataSetError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}