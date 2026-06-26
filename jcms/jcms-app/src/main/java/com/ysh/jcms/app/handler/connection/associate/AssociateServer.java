package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.node.InnerServer;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.svc.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.scl.model.document.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.security.GmAuthenticator;
import com.ysh.jcms.utils.security.SecurityContext;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.AssociationIdGenerator;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Server-side handler for incoming Associate-RequestPDU.
 *
 * <p>Handles SCL access point resolution and optional GM authentication.
 */
public class AssociateServer extends BaseServerHandler {

    private GmAuthenticator authenticator;
    private boolean requireAuthentication;
    private byte[] serverCertificateBytes;

    public AssociateServer() {
        super(ServiceName.ASSOCIATE);
    }

    public AssociateServer enableSecurity(SecurityContext ctx) throws Exception {
        this.authenticator = ctx.authenticator();
        this.requireAuthentication = true;
        this.serverCertificateBytes = ctx.certificate().getEncoded();
        return this;
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsAssociateRequest req = new CmsAssociateRequest();
        if (!tryDecode(session, request, req)) {
            return buildAssociateError(0, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        int reqId = req.reqId.value();
        log.info("Associate request from {}: reqId={}", session.getSessionId(), reqId);

        if (session.isAssociated()) {
            return buildAssociateError(reqId, CmsServiceError.INSTANCE_IN_USE);
        }

        String sapRef = req.sapRefPresent.value() && req.sapRef.len > 0
                ? new String(req.sapRef.value(), StandardCharsets.UTF_8) : null;

        if (sapRef == null || sapRef.isEmpty()) {
            log.warn("No serverAccessPointReference in request");
            return buildAssociateError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }
        log.debug("Requested access point: {}", sapRef);

        if (!resolveAndSetSclAccessPoint(session, sapRef)) {
            log.warn("Access point not found or unavailable: {}", sapRef);
            return buildAssociateError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (requireAuthentication) {
            int authError = validateAuthParam(req, sapRef);
            if (authError != CmsServiceError.NO_ERROR) {
                return buildAssociateError(reqId, authError);
            }
        }

        byte[] assocId = AssociationIdGenerator.generate();

        CmsAssociateResponse resp = new CmsAssociateResponse()
            .reqId(reqId)
            .assocId(assocId)
            .serviceError(CmsServiceError.NO_ERROR);

        if (serverCertificateBytes != null) {
            resp.authParam(new CmsAuthenticationParameter()
                .cert(serverCertificateBytes));
            resp.authParamPresent(true);
        }

        byte[] respBytes;
        try {
            respBytes = resp.encode();
        } catch (Exception e) {
            log.error("Failed to encode AssociateResponse", e);
            return buildAssociateError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", session.getSessionId());

        return buildSuccess(respBytes, reqId);
    }

    /**
     * Parse sapRef ("IEDName/AccessPointName") and set the SCL access point on the session.
     *
     * @return true if the access point was resolved and set, false otherwise
     */
    private boolean resolveAndSetSclAccessPoint(Session session, String sapRef) {
        if (!(session instanceof InnerServer.ServerSession)) return true;
        InnerServer.ServerSession ss = (InnerServer.ServerSession) session;

        SclDocument scl = ss.getSclDocument();
        if (scl == null) {
            log.warn("No SCL document loaded, skipping access point resolution for {}", sapRef);
            return true;
        }

        int slashIdx = sapRef.indexOf('/');
        String iedName = slashIdx >= 0 ? sapRef.substring(0, slashIdx) : sapRef;
        String apName = slashIdx >= 0 ? sapRef.substring(slashIdx + 1) : "S1";

        for (SclIED ied : scl.getIeds()) {
            if (ied.getName().equals(iedName)) {
                SclAccessPoint ap = ied.findAccessPointByName(apName);
                if (ap != null) {
                    ss.setSclAccessPoint(ap);
                    ss.setSclDataTypeTemplates(scl.getDataTypeTemplates());
                    log.info("Resolved SCL access point: IED={}, AP={}", iedName, apName);
                    return true;
                } else {
                    log.warn("Access point '{}' not found in IED '{}'", apName, iedName);
                    return false;
                }
            }
        }
        log.warn("IED '{}' not found in SCL document", iedName);
        return false;
    }

    private int validateAuthParam(CmsAssociateRequest req, String sapRef) {
        if (!req.authParamPresent.value() || req.authParam.cert.len == 0) {
            log.warn("Authentication required but no certificate provided");
            return CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE;
        }

        if (authenticator != null) {
            byte[] signedData = prepareSignedData(sapRef, req);
            Optional<CmsServiceError> authError = authenticator.validate(req.authParam, signedData);
            if (authError.isPresent()) {
                log.warn("Authentication failed: {}", authError.get());
                return authError.get().value();
            }
            log.debug("GM authentication successful for {}", sapRef);
        }
        return CmsServiceError.NO_ERROR;
    }

    private byte[] prepareSignedData(String sapRef, CmsAssociateRequest req) {
        byte[] sapBytes = sapRef.getBytes(StandardCharsets.UTF_8);
        if (req.authParamPresent.value() && req.authParam.signedTime != null) {
            byte[] timeBytes = String.valueOf(req.authParam.signedTime.secondsSinceEpoch.value())
                .getBytes(StandardCharsets.UTF_8);
            byte[] result = new byte[sapBytes.length + timeBytes.length];
            System.arraycopy(sapBytes, 0, result, 0, sapBytes.length);
            System.arraycopy(timeBytes, 0, result, sapBytes.length, timeBytes.length);
            return result;
        }
        return sapBytes;
    }

    private Frame buildAssociateError(int reqId, int errorCode) {
        return buildError(new CmsAssociateResponse()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode(), reqId);
    }
}
