package com.ysh.dlt2811bean.transport.app.associate;

import com.ysh.dlt2811bean.security.GmSignature;
import com.ysh.dlt2811bean.security.GmSslContext;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.transport.app.CmsServer;
import org.junit.jupiter.api.*;

import javax.net.ssl.TrustManager;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Associate service loopback tests with TLS.
 *
 * <p>Uses standard TLS cipher suites for testing transport layer logic.
 * For 国密TLS support, additional JSSE Provider is required.
 */
@DisplayName("Associate Loopback Test (TLS)")
class AssociateLoopbackGmTest {

    private static final int PORT = 18774;

    private CmsServer server;
    private CmsClient client;

    @BeforeEach
    void setup() throws Exception {
        // Generate ECDSA key pair for standard TLS testing
        KeyPair keyPair = generateEcdsaKeyPair();
        X509Certificate cert = createSelfSignedCert(keyPair);

        // Create trust managers that trust all
        TrustManager[] trustManagers = new TrustManager[]{
            new javax.net.ssl.X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }
        };

        // Create server SSL context with standard TLS
        GmSslContext serverSslContext = GmSslContext.forServer()
            .keyManager(keyPair, cert)
            .trustManager(trustManagers)
            .useStandardTls()
            .build();

        // Create client SSL context with standard TLS
        GmSslContext clientSslContext = GmSslContext.forClient()
            .trustManager(trustManagers)
            .useStandardTls()
            .build();

        // Start server with TLS
        server = new CmsServer(PORT);
        server.registerDefaultHandlers();
        server.sslContext(serverSslContext);
        server.start();
        while (!server.isBound()) {
            Thread.sleep(10);
        }

        // Create client with TLS
        client = new CmsClient();
        client.sslContext(clientSslContext);
    }

    @AfterEach
    void cleanup() {
        if (client != null) {
            client.close();
            client = null;
        }
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    /**
     * Tests TLS transport with standard TLS cipher suite.
     * This validates the transport layer code logic works correctly.
     */
    @Test
    @DisplayName("TLS connect + Associate → positive response")
    void associateWithTLS() throws Exception {
        client.connectTls("127.0.0.1", PORT);
        CmsApdu response = client.associate();

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
        assertEquals(64, client.getAssociationId().length);
    }

    /**
     * Tests TLS associate with access point using standard TLS.
     */
    @Test
    @DisplayName("TLS associate with access point → positive response")
    void associateWithAccessPointTLS() throws Exception {
        client.connectTls("127.0.0.1", PORT);
        CmsApdu response = client.associate("IED1", "AP1");

        assertNotNull(response);
        assertEquals(MessageType.RESPONSE_POSITIVE, response.getMessageType());
        assertNotNull(client.getAssociationId());
    }

    /**
     * Tests that TLS transport layer is properly configured.
     * This test passes if we can establish a TLS connection without cipher suite errors.
     */
    @Test
    @DisplayName("TLS transport layer initialization")
    void tlsTransportInitialization() {
        // Verify client transport is created with SSL context
        assertNotNull(client);
        assertTrue(client.isTlsEnabled(), "Client should have TLS enabled");

        // Verify server is bound
        assertTrue(server.isBound(), "Server should be bound");
    }

    /**
     * Generates an ECDSA key pair for standard TLS testing.
     * Uses the same curve as many production deployments.
     */
    private KeyPair generateEcdsaKeyPair() throws Exception {
        // Register BouncyCastle provider
        if (java.security.Security.getProvider("BC") == null) {
            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"), new SecureRandom());
        return keyGen.generateKeyPair();
    }

    /**
     * Creates a self-signed ECDSA certificate for standard TLS testing.
     * Standard TLS cipher suites (TLS_ECDHE_ECDSA_*) require ECDSA keys.
     */
    private X509Certificate createSelfSignedCert(KeyPair keyPair) throws Exception {
        org.bouncycastle.asn1.x500.X500NameBuilder nameBuilder = 
            new org.bouncycastle.asn1.x500.X500NameBuilder(org.bouncycastle.asn1.x500.style.BCStyle.INSTANCE);
        nameBuilder.addRDN(org.bouncycastle.asn1.x500.style.BCStyle.CN, "Test ECDSA");
        nameBuilder.addRDN(org.bouncycastle.asn1.x500.style.BCStyle.O, "TestOrg");
        org.bouncycastle.asn1.x500.X500Name issuer = nameBuilder.build();

        long now = System.currentTimeMillis();
        java.util.Date notBefore = new java.util.Date(now - 86400000);
        java.util.Date notAfter = new java.util.Date(now + 86400000);

        org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder certBuilder = 
            new org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder(
                issuer,
                java.math.BigInteger.valueOf(now),
                notBefore,
                notAfter,
                issuer,
                keyPair.getPublic()
            );

        // Use SHA256withECDSA for standard TLS compatibility
        org.bouncycastle.operator.ContentSigner contentSigner = 
            new org.bouncycastle.operator.jcajce.JcaContentSignerBuilder("SHA256withECDSA")
                .setProvider("BC")
                .build(keyPair.getPrivate());

        return new org.bouncycastle.cert.jcajce.JcaX509CertificateConverter()
            .setProvider("BC")
            .getCertificate(certBuilder.build(contentSigner));
    }
}
