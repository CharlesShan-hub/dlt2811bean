package com.ysh.jcms.core.data.sequence.connection;

import com.ysh.jcms.data.InnerAssociateRequestPDUAuthenticationParameter;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.core.data.core.CmsField;
import com.ysh.jcms.core.data.scalar.CmsOctetString;
import com.ysh.jcms.core.data.core.CmsSequence;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;

/**
 * <pre>
 * {@code
 * AuthenticationParameter ::= SEQUENCE {
 *     signatureCertificate    [0] IMPLICIT OCTET STRING,
 *     signedTime              [1] IMPLICIT UtcTime,
 *     signedValue             [2] IMPLICIT OCTET STRING
 * } — 8.2.1
 * }
 * </pre>
 *
 * <p>
 * Used by CmsAssociateRequest and CmsAssociateResponse. Uses
 * {@link InnerAssociateRequestPDUAuthenticationParameter} as the backing
 * Inner*.
 */
public class CmsAuthenticationParameter extends CmsSequence {

    @CmsField
    public CmsOctetString signatureCertificate;
    @CmsField
    public CmsUtcTime signedTime;
    @CmsField
    public CmsOctetString signedValue;

    public CmsAuthenticationParameter() {
        super(new InnerAssociateRequestPDUAuthenticationParameter());
    }

    public CmsAuthenticationParameter(InnerBase inner) {
        super(inner);
    }

    public CmsAuthenticationParameter signatureCertificate(byte[] v) {
        this.signatureCertificate.value(v);
        return this;
    }

    public CmsAuthenticationParameter signedTime(CmsUtcTime v) {
        this.signedTime.value(v);
        return this;
    }

    public CmsAuthenticationParameter signedTimeSeconds(long epochSeconds) {
        this.signedTime.secondsSinceEpoch(epochSeconds);
        return this;
    }

    public CmsAuthenticationParameter signedValue(byte[] v) {
        this.signedValue.value(v);
        return this;
    }

    public CmsAuthenticationParameter value(CmsAuthenticationParameter v) {
        return signatureCertificate(v.signatureCertificate.value()).signedTime(v.signedTime).signedValue(v.signedValue.value());
    }
}
