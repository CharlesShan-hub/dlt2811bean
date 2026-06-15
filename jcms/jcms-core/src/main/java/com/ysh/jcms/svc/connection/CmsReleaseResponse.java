package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsAssociationId;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Release-ResponsePDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     associationId   [0] IMPLICIT OCTET STRING (SIZE(0..64)),
 *     serviceError    [1] IMPLICIT ServiceError
 * }  —  8.2.2
 */
public class CmsReleaseResponse extends CmsType {

    public CmsReqId         reqId;
    public CmsAssociationId assocId;
    public CmsServiceError  serviceError;

    public CmsReleaseResponse() {
        this.reqId        = new CmsReqId();
        this.assocId      = new CmsAssociationId();
        this.serviceError = new CmsServiceError();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, assocId, serviceError);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeReleaseResponse(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeReleaseResponse(nativePtr, data); read(); }
}
