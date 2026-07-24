package com.ysh.jcms.svc.log;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * QueryLogByTime-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.8.4
 */
public class CmsQueryLogByTimeError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsQueryLogByTimeError() {
        super(Codec.QUERY_LOG_BY_TIME_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsQueryLogByTimeError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsQueryLogByTimeError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
