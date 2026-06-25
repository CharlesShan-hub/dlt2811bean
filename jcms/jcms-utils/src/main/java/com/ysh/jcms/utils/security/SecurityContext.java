package com.ysh.jcms.utils.security;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

/**
 * 安全上下文 — 封装客户端和服务端双向认证所需的所有材料。
 *
 * <p>客户端用 {@link #credentialManager()} 获取私钥和证书，
 * 服务端用 {@link #authenticator()} 验证客户端签名。
 *
 * <pre>
 * // 服务端启用安全
 * SecurityContext ctx = SecurityContext.generateSelfSigned();
 * new AssociateServer().enableSecurity(ctx);
 *
 * // 客户端启用安全
 * new AssociateClient(node, ctx);
 * </pre>
 */
public class SecurityContext {

    private final GmCredentialManager credentialManager;
    private final GmAuthenticator authenticator;
    private final X509Certificate certificate;

    private SecurityContext(GmCredentialManager cm, GmAuthenticator auth, X509Certificate cert) {
        this.credentialManager = cm;
        this.authenticator = auth;
        this.certificate = cert;
    }

    /**
     * 创建一个自签名安全上下文（含密钥对 + 证书 + trust-all 认证器）。
     * 适用于回路测试和开发环境。
     */
    public static SecurityContext generateSelfSigned() throws Exception {
        KeyPair kp = GmSignature.generateKeyPair();
        X509Certificate cert = GmSignature.generateSelfSignedCertificate(kp);
        GmCredentialManager cm = GmCredentialManager.fromKeyAndCert(kp.getPrivate(), cert);
        GmAuthenticator auth = new GmAuthenticator(new GmTrustManager().trustAll());
        return new SecurityContext(cm, auth, cert);
    }

    /** 客户端凭证（私钥 + 证书）。 */
    public GmCredentialManager credentialManager() { return credentialManager; }

    /** 服务端认证器。 */
    public GmAuthenticator authenticator() { return authenticator; }

    /** 服务端证书（响应中带回给客户端）。 */
    public X509Certificate certificate() { return certificate; }
}
