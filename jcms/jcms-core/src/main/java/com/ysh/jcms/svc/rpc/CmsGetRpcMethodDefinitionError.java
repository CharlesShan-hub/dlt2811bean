package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcMethodDefinition-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.13.5
 */
public class CmsGetRpcMethodDefinitionError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetRpcMethodDefinitionError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsGetRpcMethodDefinitionError reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetRpcMethodDefinitionError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcMethodDefinitionError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcMethodDefinitionError(nativePtr, data); read(); }
}