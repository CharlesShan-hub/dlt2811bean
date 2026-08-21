package com.ysh.jcms.utils.security;

import com.ysh.jcms.utils.config.CmsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * 安全上下文 — 封装客户端和服务端双向认证所需的所有材料。
 *
 * <p>
 * 客户端用 {@link #credentialManager()} 获取私钥和证书签名， 服务端认证由 {@code AssociateServer} 从
 * {@code application.yaml} 的 {@code server.requireAuthentication} 自动启用。
 *
 * <pre>
 * // 客户端启用安全
 * SecurityContext ctx = SecurityContext.generateSelfSigned();
 * node.setCredentialManager(ctx.credentialManager());
 * new AssociateClient(node);
 * </pre>
 */
public class SecurityContext {

    private static final Logger log = LoggerFactory.getLogger(SecurityContext.class);

    private final GmCredentialManager credentialManager;
    private final GmAuthenticator authenticator;
    private final X509Certificate certificate;

    private SecurityContext(GmCredentialManager cm, GmAuthenticator auth, X509Certificate cert) {
        this.credentialManager = cm;
        this.authenticator = auth;
        this.certificate = cert;
    }

    /**
     * 创建一个自签名安全上下文（含密钥对 + 证书 + trust-all 认证器）。 适用于回路测试和开发环境。
     */
    public static SecurityContext generateSelfSigned() throws Exception {
        KeyPair kp = GmSignature.generateKeyPair();
        X509Certificate cert = GmSignature.generateSelfSignedCertificate(kp);
        GmCredentialManager cm = GmCredentialManager.fromKeyAndCert(kp.getPrivate(), cert);
        GmAuthenticator auth = new GmAuthenticator(new GmTrustManager().trustAll());
        return new SecurityContext(cm, auth, cert);
    }

    /**
     * 从配置文件加载安全上下文。
     *
     * <p>
     * 从 {@code security.truststore.path} 加载 CA 证书，从 {@code security.keystore.path}
     * 加载 本地证书和私钥。启用真实 CA 验证（不信任自签名证书）。
     */
    public static SecurityContext fromConfig(CmsConfig config) throws Exception {
        CmsConfig.Security sec = config.security();
        if (!sec.enabled()) {
            if (sec.required())
                log.warn("security.required=true is ignored because security.enabled=false");
            return generateSelfSigned();
        }

        // 自动生成缺失的 CA 证书与本地密钥库（首次运行开箱即用）
        ensureCertificates(sec);

        // 加载 CA 证书（truststore）
        X509Certificate caCert = loadCertificate(sec.cert().caPath());

        // 构造信任管理器（加入 CA 证书）
        GmTrustManager trustManager = new GmTrustManager().addTrustedCertificate(caCert);

        // 加载本地证书和私钥（keystore）
        GmCredentialManager cm = GmCredentialManager.forServer(sec.cert().keystorePath(), sec.cert().password(), null);

        // 构造认证器（CA 验证 + 时间检查）
        long timeTolerance = sec.timeTolerance();
        GmAuthenticator auth = new GmAuthenticator(trustManager, timeTolerance);

        return new SecurityContext(cm, auth, cm.getCertificate());
    }

    /**
     * Ensures the CA certificate and local keystore exist. If either is missing,
     * generates a fresh SM2 CA and a CA-issued local certificate + keystore.
     *
     * <p>
     * CA certificate is written as DER to {@code security.truststore.path}; the
     * local certificate + private key (+ CA chain) as PKCS12 to
     * {@code security.keystore.path}.
     */
    private static void ensureCertificates(CmsConfig.Security sec) throws Exception {
        String caPath = sec.cert().caPath();
        String ksPath = sec.cert().keystorePath();
        String password = sec.cert().password();
        boolean caExists = Files.exists(Paths.get(caPath));
        boolean ksExists = Files.exists(Paths.get(ksPath));
        if (caExists && ksExists)
            return;

        // 1. CA key pair + self-signed CA certificate
        KeyPair caKeyPair = GmSignature.generateKeyPair();
        X509Certificate caCert = GmSignature.generateSelfSignedCertificate(caKeyPair, "CN=CMS Test CA");

        // 2. Local key pair + CA-issued local certificate
        KeyPair localKeyPair = GmSignature.generateKeyPair();
        X509Certificate localCert = GmSignature.issueCertificate(caKeyPair, localKeyPair.getPublic(), "CN=CMS Local");

        if (!caExists) {
            Path p = Paths.get(caPath);
            if (p.getParent() != null)
                Files.createDirectories(p.getParent());
            Files.write(p, caCert.getEncoded());
            log.info("Generated CA certificate: {}", p.toAbsolutePath());
        }

        if (!ksExists) {
            Path p = Paths.get(ksPath);
            if (p.getParent() != null)
                Files.createDirectories(p.getParent());
            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            // Use the real password when initializing the empty keystore; otherwise
            // BC stores it without a valid MAC and JDK/BouncyCastle can't reload it.
            ks.load(null, password.toCharArray());
            ks.setKeyEntry("cms", localKeyPair.getPrivate(), password.toCharArray(),
                    new Certificate[]{localCert, caCert});
            try (FileOutputStream out = new FileOutputStream(p.toFile())) {
                ks.store(out, password.toCharArray());
            }
            log.info("Generated local keystore: {}", p.toAbsolutePath());
        }
    }

    /** 客户端凭证（私钥 + 证书）。 */
    public GmCredentialManager credentialManager() {
        return credentialManager;
    }

    /** 服务端认证器。 */
    public GmAuthenticator authenticator() {
        return authenticator;
    }

    /** 服务端证书（响应中带回给客户端）。 */
    public X509Certificate certificate() {
        return certificate;
    }

    private static X509Certificate loadCertificate(String path) throws Exception {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
        try (InputStream in = new FileInputStream(path)) {
            return (X509Certificate) cf.generateCertificate(in);
        }
    }
}
