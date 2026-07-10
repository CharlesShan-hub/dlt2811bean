package com.ysh.jcms.svc.data;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataDefinition-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.4.4
 */
public class CmsGetDataDefinitionError extends CmsType {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetDataDefinitionError() {
        super(Codec.GET_DATA_DEFINITION_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetDataDefinitionError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataDefinitionError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
