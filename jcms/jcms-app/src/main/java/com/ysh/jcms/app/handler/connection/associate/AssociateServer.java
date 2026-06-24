package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.svc.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.security.GmAuthenticator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.service.ServiceHandler;
import com.ysh.jcms.utils.transport.session.AssociationIdGenerator;
import com.ysh.jcms.utils.transport.session.Session;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Optional;

/**
 * Server-side handler for incoming Associate-RequestPDU.
 *
 * <p>Handles SCL access point resolution and optional GM authentication.
 */
public class AssociateServer implements ServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(AssociateServer.class);

    private GmAuthenticator authenticator;
    private boolean requireAuthentication;
    private byte[] serverCertificateBytes;

    public AssociateServer enableSecurity(GmAuthenticator authenticator, X509Certificate serverCert) throws Exception {
        this.authenticator = authenticator;
        this.requireAuthentication = true;
        this.serverCertificateBytes = serverCert.getEncoded();
        return this;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ASSOCIATE;
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        // 1. Decode request
        CmsAssociateRequest req = new CmsAssociateRequest();
        try {
            req.decode(request.asduBytes());
        } catch (Exception e) {
            log.error("Failed to decode AssociateRequest", e);
            return buildErrorResponse(request.reqId(), CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        int reqId = req.reqId.value();
        log.info("Associate request from {}: reqId={}", session.getSessionId(), reqId);

        if (session.isAssociated()) {
            return buildErrorResponse(reqId, CmsServiceError.INSTANCE_IN_USE);
        }

        // 2. Resolve server access point reference
        String sapRef = req.sapRefPresent.value() && req.sapRef.len > 0
                ? new String(req.sapRef.value(), StandardCharsets.UTF_8) : null;

        if (sapRef == null || sapRef.isEmpty()) {
            log.warn("No serverAccessPointReference in request");
            return buildErrorResponse(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }
        log.debug("Requested access point: {}", sapRef);

        // 3. Validate authentication parameter (if required)
        if (requireAuthentication) {
            int authError = validateAuthParam(req, sapRef);
            if (authError != CmsServiceError.NO_ERROR) {
                return buildErrorResponse(reqId, authError);
            }
        }

        // 4. Generate association ID
        byte[] assocId = AssociationIdGenerator.generate();

        // 5. Build positive response
        CmsAssociateResponse resp = new CmsAssociateResponse()
            .reqId(reqId)
            .assocId(assocId)
            .serviceError(CmsServiceError.NO_ERROR);

        // Include server certificate for bidirectional authentication
        if (serverCertificateBytes != null) {
            resp.authParam(new CmsAuthenticationParameter()
                .cert(serverCertificateBytes));
            resp.authParamPresent(true);
        }
        resp.write();

        byte[] respBytes;
        try {
            respBytes = resp.encode();
        } catch (Exception e) {
            log.error("Failed to encode AssociateResponse", e);
            return buildErrorResponse(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", session.getSessionId());

        return new Frame(
            new FrameHeader().serviceCode(ServiceName.ASSOCIATE).resp(true).err(false),
            respBytes, reqId
        );
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

    private Frame buildErrorResponse(int reqId, int errorCode) {
        byte[] respBytes = new CmsAssociateResponse()
            .reqId(reqId)
            .serviceError(errorCode)
            .encode();

        return new Frame(
            new FrameHeader().serviceCode(ServiceName.ASSOCIATE).resp(true).err(true),
            respBytes, reqId
        );
    }
}
