package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.svc.other.CmsAssociationId;
import com.ysh.jcms.svc.other.CmsReqId;
import java.util.Arrays;
import java.util.List;

/**
 * Release-RequestPDU ::= SEQUENCE {
 *     reqId           Int16U,
 *     associationId   [0] IMPLICIT OCTET STRING (SIZE(0..64))
 * }  —  8.2.2
 */
public class CmsReleaseRequest extends CmsType {

    public CmsReqId         reqId;
    public CmsAssociationId assocId;

    public CmsReleaseRequest() {
        this.reqId   = new CmsReqId();
        this.assocId = new CmsAssociationId();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, assocId);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeReleaseRequest(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeReleaseRequest(nativePtr, data); read(); }
}
