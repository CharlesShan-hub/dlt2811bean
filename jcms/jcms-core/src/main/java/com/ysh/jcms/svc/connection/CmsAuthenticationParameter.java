package com.ysh.jcms.svc.connection;

import com.ysh.jcms.core.CmsField;
import com.ysh.jcms.core.CmsSequence;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerEmpty;
import com.ysh.jcms.data.string.CmsOctetString;
import com.ysh.jcms.data.time.CmsUtcTime;

/**
 * AuthenticationParameter ::= SEQUENCE { signatureCertificate [0] IMPLICIT
 * OCTET STRING, signedTime [1] IMPLICIT UtcTime, signedValue [2] IMPLICIT OCTET
 * STRING } — 8.2.1
 *
 * Used by CmsAssociateRequest and CmsAssociateResponse.
 * Pass matching Inner*AuthenticationParameter to constructor for auto-binding.
 */
public class CmsAuthenticationParameter extends CmsSequence {

    @CmsField public CmsOctetString signatureCertificate;
    @CmsField public CmsUtcTime signedTime;
    @CmsField public CmsOctetString signedValue;

    /** Default constructor — no inner binding (InnerEmpty). */
    public CmsAuthenticationParameter() {
        super(new InnerEmpty());
        this.signatureCertificate = new CmsOctetString();
        this.signedTime = new CmsUtcTime();
        this.signedValue = new CmsOctetString();
    }

    /** Constructor with specific Inner*AuthenticationParameter. */
    public CmsAuthenticationParameter(InnerBase inner) {
        super(inner);
    }
}
