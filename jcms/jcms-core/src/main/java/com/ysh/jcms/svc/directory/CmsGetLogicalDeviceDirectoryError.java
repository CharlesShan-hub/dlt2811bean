package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalDeviceDirectory-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.3.2
 */
public class CmsGetLogicalDeviceDirectoryError extends CmsType {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetLogicalDeviceDirectoryError() {
        super(Codec.GET_LOGICAL_DEVICE_DIRECTORY_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetLogicalDeviceDirectoryError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetLogicalDeviceDirectoryError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
