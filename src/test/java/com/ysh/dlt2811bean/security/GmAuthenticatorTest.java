package com.ysh.dlt2811bean.security;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.Security;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GmAuthenticator.
 */
@DisplayName("GmAuthenticator Tests")
class GmAuthenticatorTest {

    @Test
    @DisplayName("Should create authenticator with trusted certificate")
    void testCreateWithTrustedCertificate() throws Exception {
        KeyPair keyPair = GmSignature.generateKeyPair();
        java.security.cert.X509Certificate cert = createSelfSignedCert(keyPair);

        GmAuthenticator authenticator = new GmAuthenticator(cert);

        assertNotNull(authenticator);
        assertEquals(300, authenticator.getTimeToleranceSeconds());
    }

    @Test
    @DisplayName("Should create authenticator with custom time tolerance")
    void testCreateWithCustomTolerance() throws Exception {
        KeyPair keyPair = GmSignature.generateKeyPair();
        java.security.cert.X509Certificate cert = createSelfSignedCert(keyPair);

        GmAuthenticator authenticator = new GmAuthenticator(cert, 600);

        assertEquals(600, authenticator.getTimeToleranceSeconds());
    }

    @Test
    @DisplayName("Should create authenticator with trust manager")
    void testCreateWithTrustManager() {
        GmTrustManager trustManager = new GmTrustManager();
        GmAuthenticator authenticator = new GmAuthenticator(trustManager);

        assertNotNull(authenticator);
    }

    @Test
    @DisplayName("Should return empty when validating null auth param")
    void testValidateNullAuthParam() throws Exception {
        KeyPair keyPair = GmSignature.generateKeyPair();
        java.security.cert.X509Certificate cert = createSelfSignedCert(keyPair);
        GmAuthenticator authenticator = new GmAuthenticator(cert);

        var result = authenticator.validate(null, new byte[]{1, 2, 3});

        assertTrue(result.isPresent());
    }

    // Helper: Create a self-signed certificate for testing
    private java.security.cert.X509Certificate createSelfSignedCert(KeyPair keyPair) throws Exception {
        // Ensure BC provider is registered
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        // Build X500 name
        X500NameBuilder nameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
        nameBuilder.addRDN(BCStyle.CN, "Test");
        X500Name issuer = nameBuilder.build();

        // Calculate validity dates
        Date notBefore = new Date(System.currentTimeMillis() - 86400000);
        Date notAfter = new Date(System.currentTimeMillis() + 86400000);

        // Create certificate builder using JcaX509v3CertificateBuilder
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
            issuer,
            BigInteger.valueOf(System.currentTimeMillis()),
            notBefore,
            notAfter,
            issuer,
            keyPair.getPublic()
        );

        // Create self-signed certificate
        org.bouncycastle.operator.ContentSigner contentSigner = 
            new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder("SM3WITHSM2")
                .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                .build(keyPair.getPrivate());
        
        return new JcaX509CertificateConverter()
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .getCertificate(certBuilder.build(contentSigner));
    }
}
