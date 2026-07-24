package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetServerDirectory-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.3.1
 */
public class CmsGetServerDirectoryError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetServerDirectoryError() {
        super(Codec.GET_SERVER_DIRECTORY_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetServerDirectoryError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetServerDirectoryError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
