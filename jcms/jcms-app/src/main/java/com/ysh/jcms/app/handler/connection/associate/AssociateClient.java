package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.core.pdu.connection.CmsAssociateError;
import com.ysh.jcms.core.pdu.connection.CmsAssociateResponse;
import com.ysh.jcms.core.data.sequence.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.security.*;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.time.Instant;

public class AssociateClient extends BaseClientHandler<AssociateDao> {

    @Override
    public void execute(AssociateDao dao) throws Exception {
        send(ServiceName.ASSOCIATE, dao);
    }

    @Override
    protected void beforeAll(AssociateDao dao) throws IOException {
        if (!dao.secure()) {
            dao.authParam(null);
        }
        try {
            dao.authParam(buildAuthParam(node.credentialManager(), dao.sapRef()));
        } catch (Exception e) {
            throw new IOException("Failed to build authentication parameter", e);
        }
    }

    @Override
    protected void onSuccess(Frame frame, AssociateDao dao) throws IOException {
        CmsAssociateResponse resp = decodeResp(frame, new CmsAssociateResponse());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            node.client().session().state(SessionState.DISCONNECTED);
            throw new IOException("Association rejected: serviceError=" + serviceError);
        }

        // 验证服务端返回的认证参数（标准 B.3.2 双向认证）
        if (resp.isPresent("authenticationParameter") && resp.authenticationParameter.signatureCertificate.value().length > 0) {
            try {
                validateServerAuthParam(resp.authenticationParameter, dao.sapRef());
            } catch (Exception e) {
                node.client().session().state(SessionState.DISCONNECTED);
                throw new IOException("Server authentication failed: " + e.getMessage(), e);
            }
        }

        node.client().session().associationId(resp.associationId.value()).associatedApRef(dao.sapRef()).associatedSecure(dao.secure())
                .state(SessionState.ASSOCIATED);
        BaseHandler.traceSession("Associated: " + dao.sapRef());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsAssociateError err = decodeErr(frame, new CmsAssociateError());
        node.client().session().state(SessionState.DISCONNECTED);
        throw new IOException("Association rejected: error=" + err.value());
    }

    private CmsAuthenticationParameter buildAuthParam(GmCredentialManager cm, String sapRef) throws Exception {
        byte[] certBytes = cm.getCertificate().getEncoded();
        byte[] sapBytes = sapRef.getBytes(StandardCharsets.UTF_8);
        long now = System.currentTimeMillis();
        byte[] timeBytes = String.valueOf(now / 1000).getBytes(StandardCharsets.UTF_8);

        byte[] signedData = new byte[sapBytes.length + timeBytes.length];
        System.arraycopy(sapBytes, 0, signedData, 0, sapBytes.length);
        System.arraycopy(timeBytes, 0, signedData, sapBytes.length, timeBytes.length);

        byte[] signatureValue = GmSignature.sign(cm.getPrivateKey(), signedData);

        return new CmsAuthenticationParameter().signatureCertificate(certBytes).signedTime(new CmsUtcTime().now())
                .signedValue(signatureValue);
    }

    /**
     * 验证服务端返回的 authParam（证书 + 签名 + 时间）。
     */
    private void validateServerAuthParam(CmsAuthenticationParameter authParam, String sapRef) throws Exception {
        // 1. 解析服务端证书
        byte[] certBytes = authParam.signatureCertificate.value();
        X509Certificate serverCert = GmCertificateParser.parseX509(certBytes);

        // 2. 验证证书有效性
        serverCert.checkValidity();

        // 3. 如果配置了 CA，验证证书是否由 CA 签发
        CmsConfig.Security sec = CmsConfigLoader.load().security();
        if (sec.enabled()) {
            X509Certificate caCert = loadCaCertificate(sec.truststore().path());
            try {
                serverCert.verify(caCert.getPublicKey());
                log.info("Server certificate verified against CA: {}", caCert.getSubjectX500Principal().getName());
            } catch (Exception e) {
                throw new SecurityException("Server certificate not signed by trusted CA", e);
            }
        }

        // 4. 验证签名时间（防重放）
        long timeTolerance = sec.enabled() ? sec.timeTolerance() : 300;
        if (authParam.signedTime != null) {
            long signedTime = authParam.signedTime.secondsSinceEpoch.value();
            long diff = Math.abs(Instant.now().getEpochSecond() - signedTime);
            if (diff > timeTolerance) {
                throw new SecurityException("Server signature time out of tolerance: diff=" + diff + "s, max=" + timeTolerance + "s");
            }
        }

        // 5. 验签
        byte[] signatureValue = authParam.signedValue.value();
        if (signatureValue != null && signatureValue.length > 0 && authParam.signedTime != null) {
            // 构建被签名数据：sapRef + time（和服务器端一致）
            String currentSapRef = sapRef != null ? sapRef : "";
            long signedTime = authParam.signedTime.secondsSinceEpoch.value();
            byte[] sapBytes = currentSapRef.getBytes(StandardCharsets.UTF_8);
            byte[] timeBytes = String.valueOf(signedTime).getBytes(StandardCharsets.UTF_8);
            byte[] signedData = new byte[sapBytes.length + timeBytes.length];
            System.arraycopy(sapBytes, 0, signedData, 0, sapBytes.length);
            System.arraycopy(timeBytes, 0, signedData, sapBytes.length, timeBytes.length);

            PublicKey serverPublicKey = serverCert.getPublicKey();
            if (!GmSignature.verify(serverPublicKey, signedData, signatureValue)) {
                throw new SecurityException("Server signature verification failed");
            }
            log.info("Server signature verified");
        }
    }

    private static X509Certificate loadCaCertificate(String path) throws Exception {
        if (java.security.Security.getProvider("BC") == null) {
            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
        java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509", "BC");
        try (java.io.InputStream in = new java.io.FileInputStream(path)) {
            return (X509Certificate) cf.generateCertificate(in);
        }
    }
}
