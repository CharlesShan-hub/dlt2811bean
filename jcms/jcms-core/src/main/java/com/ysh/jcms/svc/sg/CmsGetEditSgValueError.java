package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetEditSGValue-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.6.5
 */
public class CmsGetEditSgValueError extends CmsType {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetEditSgValueError() {
        super(Codec.GET_EDIT_SG_VALUE_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetEditSgValueError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetEditSgValueError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
