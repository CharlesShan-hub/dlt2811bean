package com.ysh.dlt2811bean.security;

import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * GM SSL Context Factory.
 *
 * <p>Implements GM TLS based on BouncyCastle, supporting the following cipher suites:
 * <ul>
 *   <li>ECDHE_SM4_SM3 - Ephemeral cipher suite, key exchange using SM2 ECDHE</li>
 *   <li>ECC_SM4_SM3 - Static cipher suite, key exchange using SM2 ECC</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>
 * // Server
 * GmSslContext ctx = GmSslContext.forServer()
 *     .keyStore("server.pfx", "password")
 *     .trustStore("client.cer")
 *     .build();
 *
 * // Client
 * GmSslContext ctx = GmSslContext.forClient()
 *     .keyStore("client.pfx", "password")
 *     .trustStore("server.cer")
 *     .build();
 * </pre>
 */
public class GmSslContext {

    private static final String PROTOCOL = "TLS";

    /**
     * Protocol versions supported by GM TLS.
     * Note: GM TLS uses specific protocol identifiers, requires Bouncy Castle JSSE Provider.
     */
    private static final String[] ENABLED_PROTOCOLS = {
            "TLSv1.2"   // GM TLS based on TLS 1.1/1.2
    };

    /**
     * GM TLS supported cipher suites.
     * Cipher suite formats supported by Bouncy Castle JSSE Provider.
     * Note: Full GM TLS support requires special JSSE Provider (e.g., Aliyun gm-jsse).
     */
    private static final String[] ENABLED_CIPHER_SUITES = {
            // RFC style naming
            "TLS_ECDHE_ECDSA_WITH_SM4_SM3",
            "TLS_ECDHE_RSA_WITH_SM4_SM3",
            "TLS_ECDH_ECDSA_WITH_SM4_SM3",
            "TLS_ECDH_RSA_WITH_SM4_SM3",
            // GmSSL style naming
            "ECDHE_SM4_SM3",
            "ECC_SM4_SM3"
    };

    /**
     * Standard TLS cipher suites for testing (when GM is not available).
     */
    private static final String[] STANDARD_CIPHER_SUITES = {
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_RSA_WITH_AES_256_GCM_SHA384"
    };

    private final SSLContext sslContext;
    private final TrustManager[] trustManagers;
    private final KeyManager[] keyManagers;
    private final boolean useStandardTls;

    private GmSslContext(SSLContext sslContext, KeyManager[] keyManagers, TrustManager[] trustManagers, boolean useStandardTls) {
        this.sslContext = sslContext;
        this.keyManagers = keyManagers;
        this.trustManagers = trustManagers;
        this.useStandardTls = useStandardTls;
    }

    /**
     * Creates a server SSL context builder.
     */
    public static ServerBuilder forServer() {
        return new ServerBuilder();
    }

    /**
     * Creates a client SSL context builder.
     */
    public static ClientBuilder forClient() {
        return new ClientBuilder();
    }

    /**
     * Gets the underlying SSLContext.
     */
    public SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * Gets the KeyManager array.
     */
    public KeyManager[] getKeyManagers() {
        return keyManagers;
    }

    /**
     * Gets the TrustManager array.
     */
    public TrustManager[] getTrustManagers() {
        return trustManagers;
    }

    /**
     * Gets the enabled protocol versions array.
     *
     * @return supported protocol versions
     */
    public String[] getEnabledProtocols() {
        return ENABLED_PROTOCOLS.clone();
    }

    /**
     * Gets the enabled cipher suites array.
     *
     * @return supported cipher suites
     */
    public String[] getEnabledCipherSuites() {
        return useStandardTls ? STANDARD_CIPHER_SUITES.clone() : ENABLED_CIPHER_SUITES.clone();
    }

    /**
     * Gets the standard TLS cipher suites array (for testing).
     *
     * @return standard TLS cipher suites
     */
    public String[] getStandardCipherSuites() {
        return STANDARD_CIPHER_SUITES.clone();
    }

    // ==================== Builder ====================

    /**
     * Server builder.
     */
    public static class ServerBuilder {
        private String keyStorePath;
        private String keyStorePassword;
        private String keyPassword;
        private String trustStorePath;
        private String trustStorePassword;
        private KeyManager[] keyManagers;
        private TrustManager[] trustManagers;
        private boolean useStandardTls = false;

        public ServerBuilder keyStore(String path, String password) {
            this.keyStorePath = path;
            this.keyStorePassword = password;
            return this;
        }

        public ServerBuilder keyPassword(String password) {
            this.keyPassword = password;
            return this;
        }

        /**
         * Sets key manager directly from KeyPair and Certificate.
         *
         * @param keyPair the key pair
         * @param cert    the certificate
         * @return this builder
         * @throws Exception if creation fails
         */
        public ServerBuilder keyManager(java.security.KeyPair keyPair, java.security.cert.X509Certificate cert) throws Exception {
            this.keyManagers = createKeyManagers(keyPair, cert, useStandardTls);
            return this;
        }

        public ServerBuilder trustStore(String path, String password) {
            this.trustStorePath = path;
            this.trustStorePassword = password;
            return this;
        }

        public ServerBuilder trustCertificate(String certPath) {
            this.trustStorePath = certPath;
            this.trustStorePassword = "";
            return this;
        }

        /**
         * Sets trust manager directly.
         *
         * @param trustManagers the trust managers
         * @return this builder
         */
        public ServerBuilder trustManager(TrustManager[] trustManagers) {
            this.trustManagers = trustManagers;
            return this;
        }

        /**
         * Uses standard TLS cipher suites instead of GM suites.
         * Useful for testing or when GM JSSE provider is not available.
         *
         * @return this builder
         */
        public ServerBuilder useStandardTls() {
            this.useStandardTls = true;
            return this;
        }

        public GmSslContext build() throws Exception {
            if (useStandardTls) {
                return buildStandard();
            }
            registerProvider();

            if (keyManagers == null) {
                keyManagers = loadKeyManagers(keyStorePath, keyStorePassword, keyPassword);
            }
            if (trustManagers == null) {
                trustManagers = loadTrustManagers(trustStorePath, trustStorePassword);
            }

            SSLContext sslContext = createSslContext(keyManagers, trustManagers);

            return new GmSslContext(sslContext, keyManagers, trustManagers, false);
        }

        private GmSslContext buildStandard() throws Exception {
            // Use default SSL context for standard TLS
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagers, trustManagers, new java.security.SecureRandom());
            return new GmSslContext(sslContext, keyManagers, trustManagers, true);
        }
    }

    /**
     * Client builder.
     */
    public static class ClientBuilder {
        private String keyStorePath;
        private String keyStorePassword;
        private String keyPassword;
        private String trustStorePath;
        private String trustStorePassword;
        private KeyManager[] keyManagers;
        private TrustManager[] trustManagers;
        private boolean useStandardTls = false;

        public ClientBuilder keyStore(String path, String password) {
            this.keyStorePath = path;
            this.keyStorePassword = password;
            return this;
        }

        public ClientBuilder keyPassword(String password) {
            this.keyPassword = password;
            return this;
        }

        /**
         * Sets key manager directly from KeyPair and Certificate.
         *
         * @param keyPair the key pair
         * @param cert    the certificate
         * @return this builder
         * @throws Exception if creation fails
         */
        public ClientBuilder keyManager(java.security.KeyPair keyPair, java.security.cert.X509Certificate cert) throws Exception {
            this.keyManagers = createKeyManagers(keyPair, cert, useStandardTls);
            return this;
        }

        public ClientBuilder trustStore(String path, String password) {
            this.trustStorePath = path;
            this.trustStorePassword = password;
            return this;
        }

        public ClientBuilder trustCertificate(String certPath) {
            this.trustStorePath = certPath;
            this.trustStorePassword = "";
            return this;
        }

        /**
         * Sets trust manager directly.
         *
         * @param trustManagers the trust managers
         * @return this builder
         */
        public ClientBuilder trustManager(TrustManager[] trustManagers) {
            this.trustManagers = trustManagers;
            return this;
        }

        /**
         * Uses standard TLS cipher suites instead of GM suites.
         * Useful for testing or when GM JSSE provider is not available.
         *
         * @return this builder
         */
        public ClientBuilder useStandardTls() {
            this.useStandardTls = true;
            return this;
        }

        public GmSslContext build() throws Exception {
            if (useStandardTls) {
                return buildStandard();
            }
            registerProvider();

            if (keyManagers == null && keyStorePath != null) {
                keyManagers = loadKeyManagers(keyStorePath, keyStorePassword, keyPassword);
            }
            if (trustManagers == null) {
                trustManagers = loadTrustManagers(trustStorePath, trustStorePassword);
            }

            SSLContext sslContext = createSslContext(keyManagers, trustManagers);

            return new GmSslContext(sslContext, keyManagers, trustManagers, false);
        }

        private GmSslContext buildStandard() throws Exception {
            // Use default SSL context for standard TLS
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagers, trustManagers, new java.security.SecureRandom());
            return new GmSslContext(sslContext, keyManagers, trustManagers, true);
        }
    }

    // ==================== Internal ====================

    /**
     * Registers BouncyCastle JSSE Provider.
     */
    private static void registerProvider() {
        if (Security.getProvider(BouncyCastleJsseProvider.PROVIDER_NAME) == null) {
            Security.insertProviderAt(new BouncyCastleJsseProvider(), 1);
        }
    }

    /**
     * Loads KeyManagers.
     */
    private static KeyManager[] loadKeyManagers(String keyStorePath, String keyStorePassword, String keyPassword) throws Exception {
        if (keyStorePath == null) {
            return null;
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (InputStream is = loadResource(keyStorePath)) {
            keyStore.load(is, keyStorePassword.toCharArray());
        }

        String actualKeyPassword = (keyPassword != null) ? keyPassword : keyStorePassword;
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX", "BCJSSE");
        kmf.init(keyStore, actualKeyPassword.toCharArray());

        return kmf.getKeyManagers();
    }

    /**
     * Creates KeyManagers from KeyPair and Certificate.
     */
    private static KeyManager[] createKeyManagers(java.security.KeyPair keyPair, java.security.cert.X509Certificate cert) throws Exception {
        return createKeyManagers(keyPair, cert, false);
    }

    /**
     * Creates KeyManagers from KeyPair and Certificate.
     * @param keyPair key pair
     * @param cert certificate
     * @param useStandardTls whether to use standard TLS
     */
    static KeyManager[] createKeyManagers(java.security.KeyPair keyPair, java.security.cert.X509Certificate cert, boolean useStandardTls) throws Exception {
        if (!useStandardTls) {
            registerProvider();
        }
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setKeyEntry("key", keyPair.getPrivate(), "".toCharArray(), new java.security.cert.Certificate[]{cert});

        String algorithm = useStandardTls ? KeyManagerFactory.getDefaultAlgorithm() : "PKIX";
        String provider = useStandardTls ? "BC" : "BCJSSE";
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm, provider);
        kmf.init(keyStore, "".toCharArray());

        return kmf.getKeyManagers();
    }

    /**
     * Loads TrustManagers.
     */
    private static TrustManager[] loadTrustManagers(String trustStorePath, String trustStorePassword) throws Exception {
        if (trustStorePath == null) {
            // Use default TrustManager, trust all certificates (for testing only)
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
            tmf.init((KeyStore) null);
            return tmf.getTrustManagers();
        }

        // Support direct certificate file loading
        if (trustStorePath.endsWith(".cer") || trustStorePath.endsWith(".crt") || trustStorePath.endsWith(".pem")) {
            return createTrustManagersFromCert(trustStorePath);
        }

        // Support KeyStore loading
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (InputStream is = loadResource(trustStorePath)) {
            trustStore.load(is, trustStorePassword.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
        tmf.init(trustStore);

        return tmf.getTrustManagers();
    }

    /**
     * Creates TrustManagers from certificate file.
     */
    private static TrustManager[] createTrustManagersFromCert(String certPath) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        Certificate cert;
        try (InputStream is = loadResource(certPath)) {
            cert = cf.generateCertificate(is);
        }

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        trustStore.setCertificateEntry("server", cert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
        tmf.init(trustStore);

        return tmf.getTrustManagers();
    }

    /**
     * Creates SSLContext.
     */
    private static SSLContext createSslContext(KeyManager[] keyManagers, TrustManager[] trustManagers) throws Exception {
        SSLContext sslContext = SSLContext.getInstance(PROTOCOL, "BCJSSE");
        sslContext.init(keyManagers, trustManagers, new java.security.SecureRandom());
        return sslContext;
    }

    /**
     * Loads resource file.
     */
    private static InputStream loadResource(String path) throws Exception {
        // Try filesystem first
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new java.io.FileInputStream(file);
        }

        // Then try classpath
        InputStream is = GmSslContext.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new Exception("Cannot find resource: " + path);
        }
        return is;
    }
}
