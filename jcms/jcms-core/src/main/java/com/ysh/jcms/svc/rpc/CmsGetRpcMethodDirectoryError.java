package com.ysh.jcms.svc.rpc;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetRpcMethodDirectory-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.13.3
 */
public class CmsGetRpcMethodDirectoryError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetRpcMethodDirectoryError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetRpcMethodDirectoryError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetRpcMethodDirectoryError(nativePtr, data); read(); }
}
