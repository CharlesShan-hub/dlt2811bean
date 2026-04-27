package com.ysh.dlt2811bean.service.svc.association.datatypes;

import com.ysh.dlt2811bean.datatypes.code.CmsTimeQuality;
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

    /**
     * Convenience method: set signatureCertificate from raw bytes.
     *
     * <p>Per the standard, the certificate must be at least 8192 bytes.
     *
     * @param bytes the certificate bytes
     * @return this
     * @throws IllegalArgumentException if bytes length exceeds 65535
     */
    public AuthenticationParameter signatureCertificate(byte[] bytes) {
        if (bytes.length > 65535) {
            throw new IllegalArgumentException(
                "signatureCertificate length " + bytes.length + " exceeds maximum 65535");
        }
        this.signatureCertificate = new CmsOctetString(bytes).max(65535);
        return this;
    }

    /**
     * Convenience method: set signatureCertificate from raw bytes with a configurable max size.
     *
     * <p>Use this overload when you need to verify that the max size meets a minimum requirement
     * (e.g. the standard mandates at least 8192 bytes). The {@code max} parameter sets both the
     * upper bound for validation and the PER constraint on the underlying {@link CmsOctetString}.
     *
     * @param bytes the certificate bytes
     * @param max   the maximum allowed length for the certificate (must be >= 8192 per the standard)
     * @return this
     * @throws IllegalArgumentException if max is less than 8192, or if bytes length exceeds max
     */
    public AuthenticationParameter signatureCertificate(byte[] bytes, int max) {
        if (max < 8192) {
            throw new IllegalArgumentException(
                "signatureCertificate max " + max + " is less than minimum 8192");
        }
        if (bytes.length > max) {
            throw new IllegalArgumentException(
                "signatureCertificate length " + bytes.length + " exceeds max " + max);
        }
        this.signatureCertificate = new CmsOctetString(bytes).max(max);
        return this;
    }

    /**
     * Convenience method: set signedValue from raw bytes.
     *
     * @param bytes the signature value bytes
     * @return this
     * @throws IllegalArgumentException if bytes length exceeds 65535
     */
    public AuthenticationParameter signedValue(byte[] bytes) {
        if (bytes.length > 65535) {
            throw new IllegalArgumentException(
                "signedValue length " + bytes.length + " exceeds maximum 65535");
        }
        this.signedValue = new CmsOctetString(bytes).max(65535);
        return this;
    }

    /**
     * Convenience method: set signedTime from raw time components.
     *
     * <p>Per the standard, the signed time precision must be less than 1 second,
     * meaning {@code fractionOfSecond} must be greater than 0.
     *
     * @param secondsSinceEpoch  UTC seconds since epoch
     * @param fractionOfSecond   fraction of second in nanoseconds (0..999999999, must be > 0)
     * @param subSecondPrecision number of significant bits of fractionOfSecond (0..24, or 31 for unspecified)
     * @return this
     * @throws IllegalArgumentException if fractionOfSecond is 0 (would imply ≥1s precision)
     */
    public AuthenticationParameter signedTime(long secondsSinceEpoch, int fractionOfSecond, int subSecondPrecision) {
        if (fractionOfSecond <= 0) {
            throw new IllegalArgumentException(
                "signedTime fractionOfSecond must be > 0, precision must be less than 1 second");
        }
        this.signedTime = new CmsUtcTime()
            .secondsSinceEpoch(secondsSinceEpoch)
            .fractionOfSecond(fractionOfSecond)
            .timeQuality(new CmsTimeQuality().setSubSecondPrecision(subSecondPrecision));
        return this;
    }
}
