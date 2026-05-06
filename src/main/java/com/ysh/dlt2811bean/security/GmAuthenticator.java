package com.ysh.dlt2811bean.security;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Optional;

/**
 * 国密认证验证器。
 *
 * <p>根据 DL/T 2811-2024 标准验证 Associate 服务中的认证参数。
 * 验证流程：
 * <ol>
 *   <li>验证签名证书格式和有效期</li>
 *   <li>验证签名时间戳（防重放攻击）</li>
 *   <li>验证签名值</li>
 * </ol>
 *
 * <p>签名数据来源（GB/T 32918.4-2016）：
 * <pre>
 * Ent = IDA || Z || M
 * 其中：
 *   IDA - 用户标识字节串
 *   Z   - 由签名方公钥推导
 *   M   - 待签名消息
 * </pre>
 *
 * <p>根据标准规定，签名验证通过后返回 null 表示成功，
 * 否则返回相应的错误码。
 */
@Slf4j
public class GmAuthenticator {

    /** 签名时间戳允许偏差（秒）- 防止重放攻击 */
    private final long timeToleranceSeconds;

    /** 信任的证书颁发者 */
    private final X509Certificate trustedCertificate;

    /** 可选的信任管理器 */
    private final GmTrustManager trustManager;

    public GmAuthenticator(X509Certificate trustedCertificate) {
        this(trustedCertificate, 300); // 默认5分钟容差
    }

    public GmAuthenticator(X509Certificate trustedCertificate, long timeToleranceSeconds) {
        this.trustedCertificate = trustedCertificate;
        this.timeToleranceSeconds = timeToleranceSeconds;
        this.trustManager = null;
    }

    public GmAuthenticator(GmTrustManager trustManager) {
        this(trustManager, 300);
    }

    public GmAuthenticator(GmTrustManager trustManager, long timeToleranceSeconds) {
        this.trustedCertificate = null;
        this.trustManager = trustManager;
        this.timeToleranceSeconds = timeToleranceSeconds;
    }

    /**
     * 验证认证参数。
     *
     * @param authParam        认证参数
     * @param signedData       签名数据（通常为 serverAccessPointReference）
     * @return 验证失败返回错误码，成功返回 Optional.empty()
     */
    public Optional<CmsServiceError> validate(AuthenticationParameter authParam, byte[] signedData) {
        // 1. 检查认证参数是否存在
        if (authParam == null || authParam.signatureCertificate() == null) {
            log.warn("Authentication parameter or certificate is missing");
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 2. 获取证书
        byte[] certBytes = authParam.signatureCertificate().get();
        X509Certificate clientCert;
        try {
            clientCert = GmCertificateParser.parseX509(certBytes);
        } catch (Exception e) {
            log.warn("Failed to parse client certificate: {}", e.getMessage());
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 3. 验证证书有效期
        try {
            clientCert.checkValidity();
        } catch (Exception e) {
            log.warn("Client certificate validation failed: {}", e.getMessage());
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 4. 信任验证
        if (trustedCertificate != null) {
            // 简单匹配：客户端证书必须与信任证书完全匹配
            if (!clientCert.equals(trustedCertificate)) {
                log.warn("Client certificate does not match trusted certificate");
                return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        } else if (trustManager != null) {
            if (!trustManager.isTrusted(clientCert)) {
                log.warn("Client certificate is not trusted");
                return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        }

        // 5. 验证签名时间戳
        if (authParam.signedTime() != null) {
            long signedTime = authParam.signedTime().secondsSinceEpoch.get();
            long currentTime = Instant.now().getEpochSecond();
            long timeDiff = Math.abs(currentTime - signedTime);

            if (timeDiff > timeToleranceSeconds) {
                log.warn("Signature timestamp out of range: diff={}s, tolerance={}s",
                         timeDiff, timeToleranceSeconds);
                return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        }

        // 6. 验证签名
        byte[] signatureValue = authParam.signedValue().get();
        PublicKey publicKey = clientCert.getPublicKey();

        if (!GmSignature.verify(publicKey, signedData, signatureValue)) {
            log.warn("Signature verification failed");
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        log.debug("Authentication parameter validated successfully");
        return Optional.empty();
    }

    /**
     * 简化验证：仅验证签名。
     *
     * @param authParam  认证参数
     * @param publicKey  客户端公钥
     * @param signedData 签名数据
     * @return 验证失败返回错误码
     */
    public Optional<CmsServiceError> validateSimple(AuthenticationParameter authParam,
                                                      PublicKey publicKey,
                                                      byte[] signedData) {
        if (authParam == null) {
            return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        byte[] signatureValue = authParam.signedValue().get();
        if (GmSignature.verify(publicKey, signedData, signatureValue)) {
            return Optional.empty();
        }

        return Optional.of(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
    }

    /**
     * 获取时间容差（秒）。
     */
    public long getTimeToleranceSeconds() {
        return timeToleranceSeconds;
    }
}
