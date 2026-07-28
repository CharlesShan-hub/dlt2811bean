package com.ysh.jcms.svc.sg;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * SelectActiveSG-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.6.1
 */
public class CmsSelectActiveSgError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsSelectActiveSgError() {
        super(Codec.SELECT_ACTIVE_SG_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsSelectActiveSgError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsSelectActiveSgError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
