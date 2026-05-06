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
 * 国密凭证管理器。
 *
 * <p>提供 SM2 证书和密钥的加载、解析、签名和验签功能。
 * 支持从 PKCS12 密钥库或 PEM/DER 证书文件加载凭证。
 *
 * <p>支持的加密套件：
 * <ul>
 *   <li>SM2 with SM3 - 国家密码管理局推荐的签名算法</li>
 *   <li>SM2 with SM4 - 加密算法</li>
 * </ul>
 *
 * <p>签名格式遵循 GB/T 32918.4-2016《信息安全技术 SM2椭圆曲线公钥密码算法》
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
     * 创建服务端凭证管理器。
     *
     * @param keyStorePath  密钥库路径 (PKCS12)
     * @param keyPassword   密钥库密码
     * @param keyAlias      密钥别名（可选，为 null 时使用第一个密钥）
     */
    public static GmCredentialManager forServer(String keyStorePath, String keyPassword, String keyAlias) {
        return loadFromKeyStore(keyStorePath, keyPassword, keyAlias);
    }

    /**
     * 创建客户端凭证管理器。
     */
    public static GmCredentialManager forClient(String keyStorePath, String keyPassword, String keyAlias) {
        return loadFromKeyStore(keyStorePath, keyPassword, keyAlias);
    }

    /**
     * 从证书文件创建只读的凭证管理器（仅用于验签）。
     *
     * @param certPath 证书文件路径 (PEM/DER)
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
     * 从证书和私钥创建凭证管理器。
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

            // 如果没有指定别名，使用第一个密钥
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
     * 验证证书有效性。
     *
     * @param validationDate 验证日期
     * @throws SecurityException 如果证书无效或已过期
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
     * 获取证书的十六进制编码（不含首尾标记）。
     */
    public String getCertificateHex() {
        try {
            return Hex.toHexString(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * 获取证书的 Base64 编码（不含首尾标记）。
     */
    public String getCertificateBase64() {
        try {
            return Base64.toBase64String(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * 获取证书指纹（SHA-256）。
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
        // 先尝试文件系统
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new java.io.FileInputStream(file);
        }

        // 再尝试 classpath
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
