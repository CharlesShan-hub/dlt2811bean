package com.ysh.jcms.svc.report;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetBRCBValues-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError ServiceError
 * } — 8.7.2
 */
public class CmsGetBrcbValuesError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetBrcbValuesError() {
        super(Codec.GET_BRCB_VALUES_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetBrcbValuesError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetBrcbValuesError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
