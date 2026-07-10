package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.node.InnerServer;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.svc.connection.CmsAssociateError;
import com.ysh.jcms.svc.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.security.GmAuthenticator;
import com.ysh.jcms.utils.security.GmSignature;
import com.ysh.jcms.utils.security.GmTrustManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.AssociationIdGenerator;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class AssociateServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(AssociateServer.class);
    private GmAuthenticator authenticator;
    private byte[] serverCertificateBytes;

    public AssociateServer() {
        super(ServiceName.ASSOCIATE, CmsAssociateRequest.class, CmsAssociateError.class);
    }

    private void ensureSecurityInitialized() {
        if (authenticator != null)
            return;
        try {
            KeyPair kp = GmSignature.generateKeyPair();
            X509Certificate cert = GmSignature.generateSelfSignedCertificate(kp);
            this.authenticator = new GmAuthenticator(new GmTrustManager().trustAll());
            this.serverCertificateBytes = cert.getEncoded();
            log.info("Server authentication initialized (self-signed, trust-all)");
        } catch (Exception e) {
            log.error("Failed to initialize server authentication", e);
        }
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsAssociateRequest req = (CmsAssociateRequest) rawReq;
        log.info("Associate request from {}: reqId={}", session.getSessionId(), reqId);

        if (session.isAssociated())
            return onDecodeError(reqId, CmsServiceError.INSTANCE_IN_USE);

        String sapRef = opt(req.sapRefPresent, req.sapRef);
        if (sapRef == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        if (!resolveAndSetSclAccessPoint(session, sapRef))
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        if (req.authParamPresent.value() && req.authParam.cert.len > 0) {
            ensureSecurityInitialized();
            int authError = validateAuthParam(req, sapRef);
            if (authError != CmsServiceError.NO_ERROR)
                return onDecodeError(reqId, authError);
        }

        byte[] assocId = AssociationIdGenerator.generate();
        CmsAssociateResponse resp = new CmsAssociateResponse().reqId(reqId).assocId(assocId).serviceError(CmsServiceError.NO_ERROR);
        if (serverCertificateBytes != null) {
            resp.authParam(new CmsAuthenticationParameter().cert(serverCertificateBytes));
            resp.authParamPresent(true);
        }

        byte[] respBytes = resp.encode();
        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", session.getSessionId());
        return buildSuccess(respBytes, reqId);
    }

    private boolean resolveAndSetSclAccessPoint(Session session, String sapRef) {
        if (!(session instanceof InnerServer.ServerSession))
            return true;
        InnerServer.ServerSession ss = (InnerServer.ServerSession) session;
        SclDocument scl = ss.getSclDocument();
        if (scl == null)
            return true;

        int slashIdx = sapRef.indexOf('/');
        String iedName = slashIdx >= 0 ? sapRef.substring(0, slashIdx) : sapRef;
        String apName = slashIdx >= 0 ? sapRef.substring(slashIdx + 1) : "S1";

        for (SclIED ied : scl.ieds()) {
            if (ied.name().equals(iedName)) {
                SclAccessPoint ap = ied.findAccessPointByName(apName);
                if (ap != null) {
                    ss.setSclAccessPoint(ap);
                    ss.setSclDataTypeTemplates(scl.dataTypeTemplates());
                    log.info("Resolved SCL access point: IED={}, AP={}", iedName, apName);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private int validateAuthParam(CmsAssociateRequest req, String sapRef) {
        if (!req.authParamPresent.value() || req.authParam.cert.len == 0)
            return CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE;

        if (authenticator != null) {
            byte[] signedData = prepareSignedData(sapRef, req);
            Optional<CmsServiceError> authError = authenticator.validate(req.authParam, signedData);
            if (authError.isPresent())
                return authError.get().value();
        }
        return CmsServiceError.NO_ERROR;
    }

    private byte[] prepareSignedData(String sapRef, CmsAssociateRequest req) {
        byte[] sapBytes = sapRef.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (req.authParamPresent.value() && req.authParam.signedTime != null) {
            byte[] timeBytes = String.valueOf(req.authParam.signedTime.secondsSinceEpoch.value())
                    .getBytes(java.nio.charset.StandardCharsets.UTF_8);
            byte[] result = new byte[sapBytes.length + timeBytes.length];
            System.arraycopy(sapBytes, 0, result, 0, sapBytes.length);
            System.arraycopy(timeBytes, 0, result, sapBytes.length, timeBytes.length);
            return result;
        }
        return sapBytes;
    }
}
