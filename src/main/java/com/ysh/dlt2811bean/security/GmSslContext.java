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

    private final SSLContext sslContext;
    private final TrustManager[] trustManagers;
    private final KeyManager[] keyManagers;

    private GmSslContext(SSLContext sslContext, KeyManager[] keyManagers, TrustManager[] trustManagers) {
        this.sslContext = sslContext;
        this.keyManagers = keyManagers;
        this.trustManagers = trustManagers;
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

        public ServerBuilder keyStore(String path, String password) {
            this.keyStorePath = path;
            this.keyStorePassword = password;
            return this;
        }

        public ServerBuilder keyPassword(String password) {
            this.keyPassword = password;
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

        public GmSslContext build() throws Exception {
            registerProvider();

            KeyManager[] keyManagers = loadKeyManagers(keyStorePath, keyStorePassword, keyPassword);
            TrustManager[] trustManagers = loadTrustManagers(trustStorePath, trustStorePassword);

            SSLContext sslContext = createSslContext(keyManagers, trustManagers);

            return new GmSslContext(sslContext, keyManagers, trustManagers);
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

        public ClientBuilder keyStore(String path, String password) {
            this.keyStorePath = path;
            this.keyStorePassword = password;
            return this;
        }

        public ClientBuilder keyPassword(String password) {
            this.keyPassword = password;
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

        public GmSslContext build() throws Exception {
            registerProvider();

            KeyManager[] keyManagers = null;
            if (keyStorePath != null) {
                keyManagers = loadKeyManagers(keyStorePath, keyStorePassword, keyPassword);
            }

            TrustManager[] trustManagers = loadTrustManagers(trustStorePath, trustStorePassword);

            SSLContext sslContext = createSslContext(keyManagers, trustManagers);

            return new GmSslContext(sslContext, keyManagers, trustManagers);
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
