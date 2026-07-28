package com.ysh.jcms.svc.dataset;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetDataSetDirectory-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.5.5
 */
public class CmsGetDataSetDirectoryError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetDataSetDirectoryError() {
        super(Codec.GET_DATA_SET_DIRECTORY_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetDataSetDirectoryError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetDataSetDirectoryError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
