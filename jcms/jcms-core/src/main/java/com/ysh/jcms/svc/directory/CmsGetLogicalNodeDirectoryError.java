package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalNodeDirectory-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.3.3
 */
public class CmsGetLogicalNodeDirectoryError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetLogicalNodeDirectoryError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogicalNodeDirectoryError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogicalNodeDirectoryError(nativePtr, data); read(); }
}
