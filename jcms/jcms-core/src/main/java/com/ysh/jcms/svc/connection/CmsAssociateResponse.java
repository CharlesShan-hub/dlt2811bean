package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAssociateResponsePDU;
import com.ysh.jcms.data.InnerAssociateResponsePDUAuthenticationParameter;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.other.CmsAssociationId;

/**
 * Associate-ResponsePDU ::= SEQUENCE {
 *     associationId                  [0] IMPLICIT OCTET STRING (SIZE (0..64)),
 *     serviceError                   [1] IMPLICIT ServiceError,
 *     authenticationParameter        [2] IMPLICIT SEQUENCE {
 *         signatureCertificate        [0] IMPLICIT OCTET STRING,
 *         signedTime                  [1] IMPLICIT UtcTime,
 *         signedValue                 [2] IMPLICIT OCTET STRING
 *     } OPTIONAL
 * } — 8.2.1
 *
 * NOTE: reqId is handled at the protocol level, not part of the ASN.1 definition.
 */
public class CmsAssociateResponse extends CmsType {

    public CmsAssociationId assocId;
    public CmsServiceError serviceError;
    public CmsAuthenticationParameter authParam;

    public CmsAssociateResponse() {
        super(new InnerAssociateResponsePDU());
        this.assocId = new CmsAssociationId();
        this.serviceError = new CmsServiceError();
        this.authParam = new CmsAuthenticationParameter();
    }

    public CmsAssociateResponse assocId(byte[] v) {
        this.assocId.value(v);
        return this;
    }
    public CmsAssociateResponse assocId(String v) {
        this.assocId.value(v);
        return this;
    }
    public CmsAssociateResponse serviceError(int v) {
        this.serviceError.value(v);
        return this;
    }
    public CmsAssociateResponse authParam(CmsAuthenticationParameter v) {
        this.authParam = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerAssociateResponsePDU inner = (InnerAssociateResponsePDU) this.inner;
        inner.associationId = assocId.value();
        inner.serviceError.value = serviceError.value();
        if (authParam != null) {
            InnerAssociateResponsePDUAuthenticationParameter ia = new InnerAssociateResponsePDUAuthenticationParameter();
            ia.signatureCertificate = authParam.signature.value();
            authParam.signedTime.syncToInner();
            ia.signedTime = (InnerUtcTime) authParam.signedTime.inner;
            ia.signedValue = authParam.signedValue.value();
            inner.authenticationParameter(ia);
        }
    }

    @Override
    public void syncFromInner() {
        InnerAssociateResponsePDU inner = (InnerAssociateResponsePDU) this.inner;
        this.assocId.value(inner.associationId);
        this.serviceError.value(inner.serviceError.value);
        if (inner.authenticationParameter != null) {
            if (authParam == null) authParam = new CmsAuthenticationParameter();
            authParam.signature.value(inner.authenticationParameter.signatureCertificate);
            authParam.signedTime.inner = inner.authenticationParameter.signedTime;
            authParam.signedTime.syncFromInner();
            authParam.signedValue.value(inner.authenticationParameter.signedValue);
        }
    }
}
