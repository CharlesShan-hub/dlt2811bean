package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerReleaseRequestPDU;
import com.ysh.jcms.svc.other.CmsAssociationId;

/**
 * Release-RequestPDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64))
 * } — 8.2.2
 *
 * NOTE: reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsReleaseRequest extends CmsType {

    public CmsAssociationId assocId;

    public CmsReleaseRequest() {
        super(new InnerReleaseRequestPDU());
        this.assocId = new CmsAssociationId();
    }

    public CmsReleaseRequest assocId(byte[] v) {
        this.assocId.value(v);
        return this;
    }
    public CmsReleaseRequest assocId(String v) {
        this.assocId.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        ((InnerReleaseRequestPDU) inner).associationId = assocId.value();
    }

    @Override
    public void syncFromInner() {
        this.assocId.value(((InnerReleaseRequestPDU) inner).associationId);
    }
}
