package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerReleaseResponsePDU;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsAssociationId;

/**
 * Release-ResponsePDU ::= SEQUENCE {
 *     associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     serviceError     [1] IMPLICIT ServiceError
 * } — 8.2.2
 *
 * NOTE: reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsReleaseResponse extends CmsType {

    public CmsAssociationId assocId;
    public CmsServiceError serviceError;

    public CmsReleaseResponse() {
        super(new InnerReleaseResponsePDU());
        this.assocId = new CmsAssociationId();
        this.serviceError = new CmsServiceError();
    }

    public CmsReleaseResponse assocId(byte[] v) {
        this.assocId.value(v);
        return this;
    }
    public CmsReleaseResponse assocId(String v) {
        this.assocId.value(v);
        return this;
    }
    public CmsReleaseResponse serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }

    @Override
    public void syncToInner() {
        InnerReleaseResponsePDU inner = (InnerReleaseResponsePDU) this.inner;
        inner.associationId = assocId.value();
        inner.serviceError.value = serviceError.value();
    }

    @Override
    public void syncFromInner() {
        InnerReleaseResponsePDU inner = (InnerReleaseResponsePDU) this.inner;
        this.assocId.value(inner.associationId);
        this.serviceError.value(inner.serviceError.value);
    }
}
