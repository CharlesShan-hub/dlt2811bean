package com.ysh.dlt2811bean.security;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Optional;

/**
 * GM Authentication Validator.
 *
 * <p>Validates authentication parameters in Associate service according to DL/T 2811-2024.
 * Verification process:
 * <ol>
 *   <li>Verify signature certificate format and validity</li>
 *   <li>Verify signature timestamp (anti-replay)</li>
 *   <li>Verify signature value</li>
 * </ol>
 *
 * <p>Signature data source (GB/T 32918.4-2016):
 * <pre>
 * Ent = IDA || Z || M
 * where:
 *   IDA - User identifier byte string
 *   Z   - Derived from signer's public key
 *   M   - Message to be signed
 * </pre>
 *
 * <p>According to the standard, returns null on successful verification,
 * otherwise returns the corresponding error code.
 */
@Slf4j
public class GmAuthenticator {

    /** Signature timestamp tolerance (seconds) - for anti-replay protection */
    private final long timeToleranceSeconds;

    /** Trusted certificate issuer */
    private final X509Certificate trustedCertificate;

    /** Optional trust manager */
    private final GmTrustManager trustManager;

    public GmAuthenticator(X509Certificate trustedCertificate) {
        this(trustedCertificate, 300); // Default 5-minute tolerance
    }

    public GmAuthenticator(X509Certificate trustedCertificate, long timeToleranceSeconds) {
        this.trustedCertificate = trustedCertificate;
        this.timeToleranceSeconds = timeToleranceSeconds;
        this.trustManager = null;
    }

    public GmAuthenticator(GmTrustManager trustManager) {
        this(trustManager, 300);
    }

    public GmAuthenticator(GmTrustManager trustManager, long timeToleranceSeconds) {
        this.trustedCertificate = null;
        this.trustManager = trustManager;
        this.timeToleranceSeconds = timeToleranceSeconds;
    }

    /**
     * Validates authentication parameters.
     *
     * @param authParam   authentication parameter
     * @param signedData  signed data (usually serverAccessPointReference)
     * @return error code on failure, Optional.empty() on success
     */
    public Optional<CmsServiceError> validate(AuthenticationParameter authParam, byte[] signedData) {
        // 1. Check if authentication parameter exists
        if (authParam == null || authParam.signatureCertificate() == null) {
            log.warn("Authentication parameter or certificate is missing");
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 2. Get certificate
        byte[] certBytes = authParam.signatureCertificate().get();
        X509Certificate clientCert;
        try {
            clientCert = GmCertificateParser.parseX509(certBytes);
        } catch (Exception e) {
            log.warn("Failed to parse client certificate: {}", e.getMessage());
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 3. Validate certificate validity period
        try {
            clientCert.checkValidity();
        } catch (Exception e) {
            log.warn("Client certificate validation failed: {}", e.getMessage());
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 4. Trust verification
        if (trustedCertificate != null) {
            // Simple match: client certificate must exactly match trusted certificate
            if (!clientCert.equals(trustedCertificate)) {
                log.warn("Client certificate does not match trusted certificate");
                return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        } else if (trustManager != null) {
            if (!trustManager.isTrusted(clientCert)) {
                log.warn("Client certificate is not trusted");
                return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        }

        // 5. Verify signature timestamp
        if (authParam.signedTime() != null) {
            long signedTime = authParam.signedTime().secondsSinceEpoch.get();
            long currentTime = Instant.now().getEpochSecond();
            long timeDiff = Math.abs(currentTime - signedTime);

            if (timeDiff > timeToleranceSeconds) {
                log.warn("Signature timestamp out of range: diff={}s, tolerance={}s",
                         timeDiff, timeToleranceSeconds);
                return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        }

        // 6. Verify signature
        byte[] signatureValue = authParam.signedValue().get();
        PublicKey publicKey = clientCert.getPublicKey();

        if (!GmSignature.verify(publicKey, signedData, signatureValue)) {
            log.warn("Signature verification failed");
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        log.debug("Authentication parameter validated successfully");
        return Optional.empty();
    }

    /**
     * Simplified validation: verifies signature only.
     *
     * @param authParam  authentication parameter
     * @param publicKey  client public key
     * @param signedData signed data
     * @return error code on failure
     */
    public Optional<CmsServiceError> validateSimple(AuthenticationParameter authParam,
                                                     PublicKey publicKey,
                                                     byte[] signedData) {
        if (authParam == null) {
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        byte[] signatureValue = authParam.signedValue().get();
        if (GmSignature.verify(publicKey, signedData, signatureValue)) {
            return Optional.empty();
        }

        return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
    }

    /**
     * Gets the time tolerance in seconds.
     */
    public long getTimeToleranceSeconds() {
        return timeToleranceSeconds;
    }
}
