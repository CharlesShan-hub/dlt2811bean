package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError ServiceError
 * } — 8.8.2
 */
public class CmsGetLcbValuesError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetLcbValuesError() {
        super(Codec.GET_LCB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetLcbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLcbValuesError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
