package com.ysh.dlt2811bean.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

/**
 * GM Certificate Parser.
 *
 * <p>Provides SM2/X.509 certificate parsing and encoding conversion.
 */
public class GmCertificateParser {

    private static final String FACTORY_TYPE = "X.509";
    private static final String PROVIDER = "BC";

    static {
        // Register BouncyCastle provider if not already registered
        if (Security.getProvider(PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * Parses X509 certificate from byte array.
     *
     * @param certBytes DER-encoded certificate bytes
     * @return X509Certificate
     * @throws CertificateException if parsing fails
     */
    public static X509Certificate parseX509(byte[] certBytes) throws CertificateException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance(FACTORY_TYPE, PROVIDER);
            return (X509Certificate) cf.generateCertificate(
                new java.io.ByteArrayInputStream(certBytes)
            );
        } catch (CertificateException e) {
            throw e;
        } catch (Exception e) {
            throw new CertificateException("Failed to parse certificate", e);
        }
    }

    /**
     * Parses certificate from Base64 string.
     */
    public static X509Certificate parseFromBase64(String base64) throws CertificateException {
        return parseX509(Base64.decode(base64));
    }

    /**
     * Parses certificate from PEM format.
     *
     * @param pem PEM format certificate string (containing -----BEGIN CERTIFICATE----- markers)
     */
    public static X509Certificate parseFromPem(String pem) throws CertificateException {
        String content = pem
            .replace("-----BEGIN CERTIFICATE-----", "")
            .replace("-----END CERTIFICATE-----", "")
            .replaceAll("\\s", "");
        return parseFromBase64(content);
    }

    /**
     * Gets the hexadecimal encoding of the certificate.
     */
    public static String toHex(X509Certificate certificate) {
        try {
            return org.bouncycastle.util.encoders.Hex.toHexString(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * Gets the Base64 encoding of the certificate.
     */
    public static String toBase64(X509Certificate certificate) {
        try {
            return Base64.toBase64String(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * Gets the PEM format string of the certificate.
     */
    public static String toPem(X509Certificate certificate) {
        try {
            return "-----BEGIN CERTIFICATE-----\n" +
                   Base64.toBase64String(certificate.getEncoded()) +
                   "\n-----END CERTIFICATE-----";
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * Gets the certificate subject name.
     */
    public static String getSubject(X509Certificate certificate) {
        return certificate.getSubjectX500Principal().getName();
    }

    /**
     * Gets the certificate issuer name.
     */
    public static String getIssuer(X509Certificate certificate) {
        return certificate.getIssuerX500Principal().getName();
    }

    /**
     * Gets the certificate serial number (hexadecimal).
     */
    public static String getSerialNumberHex(X509Certificate certificate) {
        return certificate.getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * Gets the certificate fingerprint (SHA-256).
     */
    public static String getFingerprintSha256(X509Certificate certificate) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(certificate.getEncoded());
            return org.bouncycastle.util.encoders.Hex.toHexString(digest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute fingerprint", e);
        }
    }
}
