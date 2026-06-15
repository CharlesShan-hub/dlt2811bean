package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcInterfaceDirectory-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.13.2
 */
public class CmsGetRpcInterfaceDirectoryError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetRpcInterfaceDirectoryError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcInterfaceDirectoryError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcInterfaceDirectoryError(nativePtr, data); read(); }
}
