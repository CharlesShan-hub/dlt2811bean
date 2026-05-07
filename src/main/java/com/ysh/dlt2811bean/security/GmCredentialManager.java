package com.ysh.dlt2811bean.security;

import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * GM Credential Manager.
 *
 * <p>Provides SM2 certificate and key loading, parsing, signing and verification.
 * Supports loading credentials from PKCS12 keystore or PEM/DER certificate files.
 *
 * <p>Supported cipher suites:
 * <ul>
 *   <li>SM2 with SM3 - Signature algorithm recommended by National Cryptography Administration</li>
 *   <li>SM2 with SM4 - Encryption algorithm</li>
 * </ul>
 *
 * <p>Signature format follows GB/T 32918.4-2016 "Information Security Technology SM2 Elliptic Curve Public Key Cryptographic Algorithm"
 */
@Getter
public class GmCredentialManager {

    private static final String SIGNATURE_ALGORITHM = "SM3withSM2";
    private static final String PROVIDER = "BC";

    static {
        // Register BouncyCastle provider if not already registered
        if (Security.getProvider(PROVIDER) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private final KeyStore keyStore;
    private final String keyAlias;
    private final char[] keyPassword;
    @Getter
    private final X509Certificate certificate;
    @Getter
    private final PrivateKey privateKey;
    @Getter
    private final PublicKey publicKey;

    /**
     * Creates a server credential manager.
     *
     * @param keyStorePath  keystore path (PKCS12)
     * @param keyPassword   keystore password
     * @param keyAlias      key alias (optional, uses first key if null)
     */
    public static GmCredentialManager forServer(String keyStorePath, String keyPassword, String keyAlias) {
        return loadFromKeyStore(keyStorePath, keyPassword, keyAlias);
    }

    /**
     * Creates a client credential manager.
     */
    public static GmCredentialManager forClient(String keyStorePath, String keyPassword, String keyAlias) {
        return loadFromKeyStore(keyStorePath, keyPassword, keyAlias);
    }

    /**
     * Creates a read-only credential manager from certificate file (for verification only).
     *
     * @param certPath certificate file path (PEM/DER)
     */
    public static GmCredentialManager fromCertificate(String certPath) {
        try {
            Certificate cert = loadCertificate(certPath);
            return new GmCredentialManager(null, null, null, cert, null, null);
        } catch (Exception e) {
            throw new SecurityException("Failed to load certificate: " + certPath, e);
        }
    }

    /**
     * Creates a credential manager from certificate and private key.
     */
    public static GmCredentialManager fromKeyAndCert(PrivateKey privateKey, X509Certificate certificate) {
        return new GmCredentialManager(null, null, null, certificate, privateKey, certificate.getPublicKey());
    }

    private static GmCredentialManager loadFromKeyStore(String keyStorePath, String keyPassword, String keyAlias) {
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            try (InputStream is = loadResource(keyStorePath)) {
                ks.load(is, keyPassword.toCharArray());
            }

            // If no alias specified, use the first key
            if (keyAlias == null) {
                java.util.Enumeration<String> aliases = ks.aliases();
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    if (ks.isKeyEntry(alias)) {
                        keyAlias = alias;
                        break;
                    }
                }
            }

            Certificate cert = ks.getCertificate(keyAlias);
            if (cert == null) {
                throw new SecurityException("No certificate found for alias: " + keyAlias);
            }

            Key key = ks.getKey(keyAlias, keyPassword.toCharArray());
            if (!(key instanceof PrivateKey)) {
                throw new SecurityException("Key is not a PrivateKey");
            }

            return new GmCredentialManager(ks, keyAlias, keyPassword.toCharArray(),
                    (X509Certificate) cert, (PrivateKey) key, cert.getPublicKey());

        } catch (Exception e) {
            throw new SecurityException("Failed to load key store: " + keyStorePath, e);
        }
    }

    private GmCredentialManager(KeyStore keyStore, String keyAlias, char[] keyPassword,
                                  Certificate certificate, PrivateKey privateKey, PublicKey publicKey) {
        this.keyStore = keyStore;
        this.keyAlias = keyAlias;
        this.keyPassword = keyPassword;
        this.certificate = (X509Certificate) certificate;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    /**
     * Validates certificate validity.
     *
     * @param validationDate validation date
     * @throws SecurityException if certificate is invalid or expired
     */
    public void validateCertificate(Date validationDate) {
        if (certificate == null) {
            throw new SecurityException("Certificate is not available");
        }

        try {
            certificate.checkValidity(validationDate);
        } catch (CertificateExpiredException e) {
            throw new SecurityException("Certificate has expired", e);
        } catch (CertificateNotYetValidException e) {
            throw new SecurityException("Certificate is not yet valid", e);
        }
    }

    /**
     * Gets the hexadecimal encoding of the certificate (without header/footer).
     */
    public String getCertificateHex() {
        try {
            return Hex.toHexString(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * Gets the Base64 encoding of the certificate (without header/footer).
     */
    public String getCertificateBase64() {
        try {
            return Base64.toBase64String(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * Gets the certificate fingerprint (SHA-256).
     */
    public String getCertificateFingerprint() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(certificate.getEncoded());
            return Hex.toHexString(digest);
        } catch (NoSuchAlgorithmException | java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to compute certificate fingerprint", e);
        }
    }

    private static InputStream loadResource(String path) throws Exception {
        // Try filesystem first
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new java.io.FileInputStream(file);
        }

        // Then try classpath
        InputStream is = GmCredentialManager.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new Exception("Cannot find resource: " + path);
        }
        return is;
    }

    private static Certificate loadCertificate(String certPath) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        try (InputStream is = loadResource(certPath)) {
            return cf.generateCertificate(is);
        }
    }
}
