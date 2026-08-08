package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.pdu.connection.CmsAssociateRequest;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.security.GmAuthenticator;
import com.ysh.jcms.utils.security.GmSignature;
import com.ysh.jcms.utils.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Optional;

/**
 * Associate 服务的认证与签名支持。
 *
 * <p>
 * 承载服务端安全状态的懒加载（authenticator / 私钥 / 证书）、客户端认证参数校验
 * 以及服务端证书与签名参数的构造。与 {@link AssociateServer} 分离，使主类只保留编排。
 */
public class AssociateSecurity {

    private static final Logger log = LoggerFactory.getLogger(AssociateSecurity.class);

    private GmAuthenticator authenticator;
    private PrivateKey serverPrivateKey;
    private byte[] serverCertificateBytes;

    public void ensureInitialized() {
        if (authenticator != null)
            return;
        try {
            SecurityContext ctx = SecurityContext.fromConfig(CmsConfigLoader.load());
            this.authenticator = ctx.authenticator();
            this.serverCertificateBytes = ctx.certificate().getEncoded();
            this.serverPrivateKey = ctx.credentialManager().getPrivateKey();
            String mode = CmsConfigLoader.load().security().enabled() ? "CA" : "self-signed";
            log.info("Server authentication initialized (mode={})", mode);
        } catch (Exception e) {
            log.error("Failed to initialize server authentication", e);
        }
    }

    /** 校验客户端认证参数，返回错误码；成功返回 {@link CmsServiceError#NO_ERROR}。 */
    public int validate(CmsAssociateRequest req, String sapRef) {
        if (!req.isPresent("authenticationParameter") || req.authenticationParameter.signatureCertificate.value().length == 0)
            return CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE;

        if (authenticator != null) {
            byte[] signedData = prepareSignedData(sapRef, req);
            Optional<CmsServiceError> authError = authenticator.validate(req.authenticationParameter, signedData);
            if (authError.isPresent())
                return authError.get().value();
        }
        return CmsServiceError.NO_ERROR;
    }

    /**
     * 构造服务端认证参数（证书 + 可选签名，标准 B.3.2 要求双向认证）。
     *
     * @return 认证参数；服务端证书不可用时返回 {@code null}
     */
    public CmsAuthenticationParameter buildAuthParam(String sapRef) {
        if (serverCertificateBytes == null)
            return null;
        if (serverPrivateKey != null && sapRef != null) {
            try {
                byte[] signedData = buildServerSignedData(sapRef);
                byte[] signature = GmSignature.sign(serverPrivateKey, signedData);
                return new CmsAuthenticationParameter().signatureCertificate(serverCertificateBytes)
                        .signedTime(new CmsUtcTime().now()).signedValue(signature);
            } catch (Exception e) {
                log.warn("Failed to sign server auth param", e);
            }
        }
        return new CmsAuthenticationParameter().signatureCertificate(serverCertificateBytes);
    }

    private byte[] prepareSignedData(String sapRef, CmsAssociateRequest req) {
        byte[] sapBytes = sapRef.getBytes(StandardCharsets.UTF_8);
        if (req.isPresent("authenticationParameter") && req.authenticationParameter.signedTime != null) {
            byte[] timeBytes = String.valueOf(req.authenticationParameter.signedTime.secondsSinceEpoch.value())
                    .getBytes(StandardCharsets.UTF_8);
            byte[] result = new byte[sapBytes.length + timeBytes.length];
            System.arraycopy(sapBytes, 0, result, 0, sapBytes.length);
            System.arraycopy(timeBytes, 0, result, sapBytes.length, timeBytes.length);
            return result;
        }
        return sapBytes;
    }

    private byte[] buildServerSignedData(String sapRef) {
        // 服务端签名内容：sapRef + 当前时间
        long now = System.currentTimeMillis() / 1000;
        byte[] sapBytes = sapRef.getBytes(StandardCharsets.UTF_8);
        byte[] timeBytes = String.valueOf(now).getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[sapBytes.length + timeBytes.length];
        System.arraycopy(sapBytes, 0, result, 0, sapBytes.length);
        System.arraycopy(timeBytes, 0, result, sapBytes.length, timeBytes.length);
        return result;
    }
}
