package com.ysh.jcms.svc.goose;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetGOOSEElementNumber-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.9.3
 */
public class CmsGetGooseElementNumberError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetGooseElementNumberError() {
        super(Codec.GET_GOOSE_ELEMENT_NUMBER_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetGooseElementNumberError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetGooseElementNumberError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
