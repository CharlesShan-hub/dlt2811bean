package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * RpcCall-ErrorPDU ::= SEQUENCE { reqId Int16U, serviceError ServiceError } —
 * 8.13.6
 */
public class CmsRpcCallError extends CmsType {

    public CmsReqId reqId;
    public CmsServiceError serviceError;

    public CmsRpcCallError() {
        super(Codec.RPC_CALL_ERROR);
        this.reqId = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    public CmsRpcCallError reqId(int v) {
        this.reqId.value(v);
        return this;
    }
    public CmsRpcCallError serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }
}
