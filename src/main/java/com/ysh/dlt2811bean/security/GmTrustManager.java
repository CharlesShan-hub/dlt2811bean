package com.ysh.dlt2811bean.security;

import lombok.extern.slf4j.Slf4j;

import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

/**
 * 国密信任管理器。
 *
 * <p>管理可信证书集合，支持证书指纹匹配和链式验证。
 */
@Slf4j
public class GmTrustManager {

    private final Set<String> trustedFingerprints = new HashSet<>();
    private final Set<X509Certificate> trustedCertificates = new HashSet<>();
    private boolean trustAll = false;  // 开发/测试模式：信任所有证书

    /**
     * 添加可信证书。
     */
    public GmTrustManager addTrustedCertificate(X509Certificate certificate) {
        trustedCertificates.add(certificate);
        trustedFingerprints.add(GmCertificateParser.getFingerprintSha256(certificate));
        return this;
    }

    /**
     * 添加可信证书（通过 PEM 字符串）。
     */
    public GmTrustManager addTrustedCertificate(String pem) throws Exception {
        X509Certificate cert = GmCertificateParser.parseFromPem(pem);
        return addTrustedCertificate(cert);
    }

    /**
     * 添加可信指纹。
     */
    public GmTrustManager addTrustedFingerprint(String sha256Fingerprint) {
        trustedFingerprints.add(sha256Fingerprint.toUpperCase());
        return this;
    }

    /**
     * 启用"信任所有"模式（用于开发/测试）。
     * 启用后，所有证书都将被视为可信。
     */
    public GmTrustManager trustAll() {
        this.trustAll = true;
        return this;
    }

    /**
     * 检查证书是否可信。
     */
    public boolean isTrusted(X509Certificate certificate) {
        if (certificate == null) {
            return false;
        }

        // 如果启用了信任所有模式，则信任所有证书
        if (trustAll) {
            log.debug("Trust-all mode enabled, accepting certificate");
            return true;
        }

        // 检查指纹
        String fingerprint = GmCertificateParser.getFingerprintSha256(certificate);
        if (trustedFingerprints.contains(fingerprint.toUpperCase())) {
            log.debug("Certificate trusted by fingerprint match");
            return true;
        }

        // 检查证书对象
        if (trustedCertificates.contains(certificate)) {
            log.debug("Certificate trusted by object match");
            return true;
        }

        return false;
    }

    /**
     * 检查证书指纹是否可信。
     */
    public boolean isTrustedFingerprint(String fingerprint) {
        return trustedFingerprints.contains(fingerprint.toUpperCase());
    }

    /**
     * 获取可信证书数量。
     */
    public int getTrustedCount() {
        return trustedCertificates.size();
    }

    /**
     * 清空可信证书。
     */
    public void clear() {
        trustedCertificates.clear();
        trustedFingerprints.clear();
    }
}
