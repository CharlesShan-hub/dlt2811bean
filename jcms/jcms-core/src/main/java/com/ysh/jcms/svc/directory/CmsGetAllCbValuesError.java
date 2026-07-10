package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetAllCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.3.6
 */
public class CmsGetAllCbValuesError extends CmsType {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetAllCbValuesError() {
        super(Codec.GET_ALL_CB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetAllCbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetAllCbValuesError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
