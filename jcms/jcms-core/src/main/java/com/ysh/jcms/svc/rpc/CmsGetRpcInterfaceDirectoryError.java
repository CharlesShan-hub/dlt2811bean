package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDirectory-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError
 * ServiceError } — 8.13.2
 */
public class CmsGetRpcInterfaceDirectoryError extends CmsTypeOld {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsGetRpcInterfaceDirectoryError() {
        super(Codec.GET_RPC_INTERFACE_DIRECTORY_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsGetRpcInterfaceDirectoryError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsGetRpcInterfaceDirectoryError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsTypeOld> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
