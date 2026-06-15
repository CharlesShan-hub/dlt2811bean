package com.ysh.jcms.svc.directory;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * GetLogicalDeviceDirectory-ErrorPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     serviceError    ServiceError
 * }  —  8.3.2
 */
public class CmsGetLogicalDeviceDirectoryError extends CmsType {

    public CmsReqId        reqId;
    public CmsServiceError serviceError;

    public CmsGetLogicalDeviceDirectoryError() {
        this.reqId        = new CmsReqId();
        this.serviceError = new CmsServiceError();
    }
    
    public CmsGetLogicalDeviceDirectoryError reqId(int v) { this.reqId.value(v); return this; }
    public CmsGetLogicalDeviceDirectoryError serviceError(int v) { this.serviceError.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeGetLogicalDeviceDirectoryError(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeGetLogicalDeviceDirectoryError(nativePtr, data); read(); }
}