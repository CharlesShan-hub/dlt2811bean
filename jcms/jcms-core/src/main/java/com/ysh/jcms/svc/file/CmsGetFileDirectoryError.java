package com.ysh.jcms.svc.file;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetFileDirectory-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.12.4
 */
public class CmsGetFileDirectoryError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetFileDirectoryError() {
        super(Codec.GET_FILE_DIRECTORY_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetFileDirectoryError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetFileDirectoryError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
