package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerAssociateRequestPDU;
import com.ysh.jcms.data.InnerAssociateRequestPDUAuthenticationParameter;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.string.CmsUint8Array;
import java.nio.charset.StandardCharsets;

/**
 * Associate-RequestPDU ::= SEQUENCE {
 *     serverAccessPointReference    [0] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,
 *     authenticationParameter       [1] IMPLICIT SEQUENCE {
 *         signatureCertificate        [0] IMPLICIT OCTET STRING,
 *         signedTime                  [1] IMPLICIT UtcTime,
 *         signedValue                 [2] IMPLICIT OCTET STRING
 *     } OPTIONAL
 * } — 8.2.1
 */
public class CmsAssociateRequest extends CmsType {

    public CmsUint8Array sapRef;
    public CmsAuthenticationParameter authParam;

    public CmsAssociateRequest() {
        super(new InnerAssociateRequestPDU());
        this.sapRef = new CmsUint8Array();
        this.authParam = new CmsAuthenticationParameter();
    }

    public CmsAssociateRequest sapRef(String v) {
        this.sapRef.value(v);
        return this;
    }
    public CmsAssociateRequest authParam(CmsAuthenticationParameter v) {
        this.authParam = v;
        return this;
    }

    @Override
    public void syncToInner() {
        InnerAssociateRequestPDU inner = (InnerAssociateRequestPDU) this.inner;
        inner.serverAccessPointReference(new String(sapRef.value(), StandardCharsets.UTF_8));
        if (authParam != null) {
            InnerAssociateRequestPDUAuthenticationParameter ia = new InnerAssociateRequestPDUAuthenticationParameter();
            ia.signatureCertificate = authParam.signature.value();
            authParam.signedTime.syncToInner();
            ia.signedTime = (InnerUtcTime) authParam.signedTime.inner;
            ia.signedValue = authParam.signedValue.value();
            inner.authenticationParameter(ia);
        }
    }

    @Override
    public void syncFromInner() {
        InnerAssociateRequestPDU inner = (InnerAssociateRequestPDU) this.inner;
        this.sapRef.value(inner.serverAccessPointReference);
        if (inner.authenticationParameter != null) {
            if (authParam == null) authParam = new CmsAuthenticationParameter();
            authParam.signature.value(inner.authenticationParameter.signatureCertificate);
            authParam.signedTime.inner = inner.authenticationParameter.signedTime;
            authParam.signedTime.syncFromInner();
            authParam.signedValue.value(inner.authenticationParameter.signedValue);
        }
    }
}
