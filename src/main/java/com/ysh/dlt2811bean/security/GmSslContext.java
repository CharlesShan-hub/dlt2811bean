package com.ysh.dlt2811bean.security;

import org.bouncycastle.jsse.provider.BouncyCastleJsseProvider;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * 国密 SSL 上下文工厂。
 *
 * <p>基于 BouncyCastle 实现国密 TLS，支持以下加密套件：
 * <ul>
 *   <li>ECDHE_SM4_SM3 - 动态加密套件，密钥交换使用 SM2 ECDHE</li>
 *   <li>ECC_SM4_SM3 - 静态加密套件，密钥交换使用 SM2 ECC</li>
 * </ul>
 *
 * <p>使用方式：
 * <pre>
 * // 服务端
 * GmSslContext ctx = GmSslContext.forServer()
 *     .keyStore("server.pfx", "password")
 *     .trustStore("client.cer")
 *     .build();
 *
 * // 客户端
 * GmSslContext ctx = GmSslContext.forClient()
 *     .keyStore("client.pfx", "password")
 *     .trustStore("server.cer")
 *     .build();
 * </pre>
 */
public class GmSslContext {

    private static final String PROTOCOL = "TLS";

    /**
     * 国密 TLS 支持的协议版本。
     * 注意：国密 TLS 使用特定的协议标识，需要 Bouncy Castle JSSE Provider 支持。
     */
    private static final String[] ENABLED_PROTOCOLS = {
            "TLSv1.2"   // 国密 TLS 基于 TLS 1.1/1.2
    };

    /**
     * 国密 TLS 支持的加密套件。
     * Bouncy Castle JSSE Provider 支持的国密套件格式。
     * 注意：完整的国密 TLS 支持需要特殊的 JSSE Provider（如阿里云 gm-jsse）。
     */
    private static final String[] ENABLED_CIPHER_SUITES = {
            // RFC 风格命名
            "TLS_ECDHE_ECDSA_WITH_SM4_SM3",
            "TLS_ECDHE_RSA_WITH_SM4_SM3",
            "TLS_ECDH_ECDSA_WITH_SM4_SM3",
            "TLS_ECDH_RSA_WITH_SM4_SM3",
            // GmSSL 风格命名
            "ECDHE_SM4_SM3",
            "ECC_SM4_SM3"
    };

    /**
     * Standard TLS cipher suites for testing (when 国密 is not available).
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
     * 创建服务端 SSL 上下文构建器。
     */
    public static ServerBuilder forServer() {
        return new ServerBuilder();
    }

    /**
     * 创建客户端 SSL 上下文构建器。
     */
    public static ClientBuilder forClient() {
        return new ClientBuilder();
    }

    /**
     * 获取底层 SSLContext。
     */
    public SSLContext getSslContext() {
        return sslContext;
    }

    /**
     * 获取 KeyManager 数组。
     */
    public KeyManager[] getKeyManagers() {
        return keyManagers;
    }

    /**
     * 获取 TrustManager 数组。
     */
    public TrustManager[] getTrustManagers() {
        return trustManagers;
    }

    /**
     * 获取启用的协议版本数组。
     *
     * @return 支持的协议版本
     */
    public String[] getEnabledProtocols() {
        return ENABLED_PROTOCOLS.clone();
    }

    /**
     * 获取启用的加密套件数组。
     *
     * @return 支持的加密套件
     */
    public String[] getEnabledCipherSuites() {
        return useStandardTls ? STANDARD_CIPHER_SUITES.clone() : ENABLED_CIPHER_SUITES.clone();
    }

    /**
     * 获取标准TLS加密套件数组（用于测试）。
     *
     * @return 标准TLS加密套件
     */
    public String[] getStandardCipherSuites() {
        return STANDARD_CIPHER_SUITES.clone();
    }

    // ==================== Builder ====================

    /**
     * 服务端构建器。
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
         * Uses standard TLS cipher suites instead of 国密 suites.
         * Useful for testing or when 国密 JSSE provider is not available.
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
     * 客户端构建器。
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
         * Uses standard TLS cipher suites instead of 国密 suites.
         * Useful for testing or when 国密 JSSE provider is not available.
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
     * 注册 BouncyCastle JSSE Provider。
     */
    private static void registerProvider() {
        if (Security.getProvider(BouncyCastleJsseProvider.PROVIDER_NAME) == null) {
            Security.insertProviderAt(new BouncyCastleJsseProvider(), 1);
        }
    }

    /**
     * 加载 KeyManager。
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
     * 从 KeyPair 和 Certificate 创建 KeyManager。
     */
    private static KeyManager[] createKeyManagers(java.security.KeyPair keyPair, java.security.cert.X509Certificate cert) throws Exception {
        return createKeyManagers(keyPair, cert, false);
    }

    /**
     * 从 KeyPair 和 Certificate 创建 KeyManager。
     * @param keyPair 密钥对
     * @param cert 证书
     * @param useStandardTls 是否使用标准TLS
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
     * 加载 TrustManager。
     */
    private static TrustManager[] loadTrustManagers(String trustStorePath, String trustStorePassword) throws Exception {
        if (trustStorePath == null) {
            // 使用默认 TrustManager，信任所有证书（仅用于测试）
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
            tmf.init((KeyStore) null);
            return tmf.getTrustManagers();
        }

        // 支持证书文件直接加载
        if (trustStorePath.endsWith(".cer") || trustStorePath.endsWith(".crt") || trustStorePath.endsWith(".pem")) {
            return createTrustManagersFromCert(trustStorePath);
        }

        // 支持 KeyStore 加载
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (InputStream is = loadResource(trustStorePath)) {
            trustStore.load(is, trustStorePassword.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX", "BCJSSE");
        tmf.init(trustStore);

        return tmf.getTrustManagers();
    }

    /**
     * 从证书文件创建 TrustManager。
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
     * 创建 SSLContext。
     */
    private static SSLContext createSslContext(KeyManager[] keyManagers, TrustManager[] trustManagers) throws Exception {
        SSLContext sslContext = SSLContext.getInstance(PROTOCOL, "BCJSSE");
        sslContext.init(keyManagers, trustManagers, new java.security.SecureRandom());
        return sslContext;
    }

    /**
     * 加载资源文件。
     */
    private static InputStream loadResource(String path) throws Exception {
        // 先尝试文件系统
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new java.io.FileInputStream(file);
        }

        // 再尝试 classpath
        InputStream is = GmSslContext.class.getClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new Exception("Cannot find resource: " + path);
        }
        return is;
    }
}
