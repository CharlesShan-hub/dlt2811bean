package com.ysh.dlt2811bean.security;

import lombok.extern.slf4j.Slf4j;

import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

/**
 * GM Trust Manager.
 *
 * <p>Manages trusted certificate collection, supports certificate fingerprint matching and chain verification.
 */
@Slf4j
public class GmTrustManager {

    private final Set<String> trustedFingerprints = new HashSet<>();
    private final Set<X509Certificate> trustedCertificates = new HashSet<>();
    private boolean trustAll = false;  // Dev/test mode: trust all certificates

    /**
     * Adds a trusted certificate.
     */
    public GmTrustManager addTrustedCertificate(X509Certificate certificate) {
        trustedCertificates.add(certificate);
        trustedFingerprints.add(GmCertificateParser.getFingerprintSha256(certificate));
        return this;
    }

    /**
     * Adds a trusted certificate (from PEM string).
     */
    public GmTrustManager addTrustedCertificate(String pem) throws Exception {
        X509Certificate cert = GmCertificateParser.parseFromPem(pem);
        return addTrustedCertificate(cert);
    }

    /**
     * Adds a trusted fingerprint.
     */
    public GmTrustManager addTrustedFingerprint(String sha256Fingerprint) {
        trustedFingerprints.add(sha256Fingerprint.toUpperCase());
        return this;
    }

    /**
     * Enables "trust all" mode (for dev/testing).
     * When enabled, all certificates will be treated as trusted.
     */
    public GmTrustManager trustAll() {
        this.trustAll = true;
        return this;
    }

    /**
     * Checks if a certificate is trusted.
     */
    public boolean isTrusted(X509Certificate certificate) {
        if (certificate == null) {
            return false;
        }

        // If trust-all mode is enabled, trust all certificates
        if (trustAll) {
            log.debug("Trust-all mode enabled, accepting certificate");
            return true;
        }

        // Check fingerprint
        String fingerprint = GmCertificateParser.getFingerprintSha256(certificate);
        if (trustedFingerprints.contains(fingerprint.toUpperCase())) {
            log.debug("Certificate trusted by fingerprint match");
            return true;
        }

        // Check certificate object
        if (trustedCertificates.contains(certificate)) {
            log.debug("Certificate trusted by object match");
            return true;
        }

        return false;
    }

    /**
     * Checks if a certificate fingerprint is trusted.
     */
    public boolean isTrustedFingerprint(String fingerprint) {
        return trustedFingerprints.contains(fingerprint.toUpperCase());
    }

    /**
     * Gets the count of trusted certificates.
     */
    public int getTrustedCount() {
        return trustedCertificates.size();
    }

    /**
     * Clears all trusted certificates.
     */
    public void clear() {
        trustedCertificates.clear();
        trustedFingerprints.clear();
    }
}
