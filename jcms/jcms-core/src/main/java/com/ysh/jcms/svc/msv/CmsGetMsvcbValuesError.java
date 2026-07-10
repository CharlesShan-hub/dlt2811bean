package com.ysh.jcms.svc.msv;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetMSVCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.10.2
 */
public class CmsGetMsvcbValuesError extends CmsType {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetMsvcbValuesError() {
        super(Codec.GET_MSVCB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetMsvcbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetMsvcbValuesError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
