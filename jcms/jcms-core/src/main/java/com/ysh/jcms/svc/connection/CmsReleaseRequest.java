package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;
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

    public CmsReleaseRequest() { super(Codec.RELEASE_REQUEST);
        this.reqId   = new CmsReqId();
        this.assocId = new CmsAssociationId();
    }
    
    public CmsReleaseRequest reqId(int v) { this.reqId.value(v); return this; }
    public CmsReleaseRequest assocId(byte[] v) { this.assocId.value(v); return this; }
    public CmsReleaseRequest assocId(String v) { this.assocId.value(v); return this; }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(reqId, assocId);
    }
}