package com.ysh.dlt2811bean.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;

/**
 * 国密证书解析器。
 *
 * <p>提供 SM2/X.509 证书的解析和编码转换功能。
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
     * 从字节数组解析 X509 证书。
     *
     * @param certBytes DER 编码的证书字节
     * @return X509Certificate
     * @throws CertificateException 如果解析失败
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
     * 从 Base64 字符串解析证书。
     */
    public static X509Certificate parseFromBase64(String base64) throws CertificateException {
        return parseX509(Base64.decode(base64));
    }

    /**
     * 从 PEM 格式解析证书。
     *
     * @param pem PEM 格式证书字符串（包含 -----BEGIN CERTIFICATE----- 标记）
     */
    public static X509Certificate parseFromPem(String pem) throws CertificateException {
        String content = pem
            .replace("-----BEGIN CERTIFICATE-----", "")
            .replace("-----END CERTIFICATE-----", "")
            .replaceAll("\\s", "");
        return parseFromBase64(content);
    }

    /**
     * 获取证书的十六进制编码。
     */
    public static String toHex(X509Certificate certificate) {
        try {
            return org.bouncycastle.util.encoders.Hex.toHexString(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * 获取证书的 Base64 编码。
     */
    public static String toBase64(X509Certificate certificate) {
        try {
            return Base64.toBase64String(certificate.getEncoded());
        } catch (java.security.cert.CertificateEncodingException e) {
            throw new SecurityException("Failed to encode certificate", e);
        }
    }

    /**
     * 获取证书的 PEM 格式字符串。
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
     * 获取证书主题名称。
     */
    public static String getSubject(X509Certificate certificate) {
        return certificate.getSubjectX500Principal().getName();
    }

    /**
     * 获取证书颁发者名称。
     */
    public static String getIssuer(X509Certificate certificate) {
        return certificate.getIssuerX500Principal().getName();
    }

    /**
     * 获取证书序列号（十六进制）。
     */
    public static String getSerialNumberHex(X509Certificate certificate) {
        return certificate.getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * 获取证书指纹（SHA-256）。
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
