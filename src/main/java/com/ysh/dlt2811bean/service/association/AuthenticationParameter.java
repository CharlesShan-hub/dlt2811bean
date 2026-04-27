package com.ysh.dlt2811bean.service.association;

import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.string.CmsOctetString;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCompound;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Authentication Parameter — security authentication data for the Associate service.
 *
 * <p>Per §8.2.1.3, the authentication parameter is an optional parameter used for
 * secure communication. When security is required, it carries digital certificate
 * information with the following fields:
 *
 * <pre>
 * ┌──────────────────────┬──────────────┬──────────────────────────────┐
 * │ Field                │ Type         │ Description                  │
 * ├──────────────────────┼──────────────┼──────────────────────────────┤
 * │ signatureCertificate │ OCTET STRING │ Digital certificate (≥8192B) │
 * │ signedTime           │ UtcTime      │ Signature UTC time (<1s)     │
 * │ signedValue          │ OCTET STRING │ Signature value              │
 * └──────────────────────┴──────────────┴──────────────────────────────┘
 * </pre>
 *
 * <p>This parameter appears in both the Associate Request and Associate Response+.
 */
@Getter
@Setter
@Accessors(fluent = true)
public class AuthenticationParameter extends AbstractCmsCompound<AuthenticationParameter> {

    public CmsOctetString signatureCertificate = new CmsOctetString().max(65535);
    public CmsUtcTime signedTime = new CmsUtcTime();
    public CmsOctetString signedValue = new CmsOctetString().max(65535);

    public AuthenticationParameter() {
        super("AuthenticationParameter");
        registerField("signatureCertificate");
        registerField("signedTime");
        registerField("signedValue");
    }
}
