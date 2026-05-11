package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.security.GmAuthenticator;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.transport.session.AssociationIdGenerator;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.SessionState;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.security.cert.X509Certificate;
import java.util.Optional;

/**
 * Handler for Associate service (SC=1).
 *
 * <p>Generates a 64-byte association ID for the client and returns it
 * in a positive response. Supports GM (Guomi) authentication per DL/T 2811-2024.
 *
 * <p>Authentication flow:
 * <ol>
 *   <li>Validate serverAccessPointReference</li>
 *   <li>If authenticationParameter present, verify SM2 signature</li>
 *   <li>Generate association ID</li>
 *   <li>Return positive/negative response</li>
 * </ol>
 */
public class AssociateHandler extends AbstractCmsServiceHandler<CmsAssociate> {

    private GmAuthenticator authenticator;
    private boolean requireAuthentication = false;
    private final SclDocument sclDocument;
    private byte[] serverCertificateBytes;

    public AssociateHandler() {
        super(ServiceName.ASSOCIATE, CmsAssociate::new);
        this.sclDocument = null;
    }

    public AssociateHandler(SclDocument sclDocument) {
        super(ServiceName.ASSOCIATE, CmsAssociate::new);
        this.sclDocument = sclDocument;
    }

    public AssociateHandler enableSecurity(GmAuthenticator authenticator, X509Certificate serverCert) throws Exception {
        this.authenticator = authenticator;
        this.requireAuthentication = true;
        this.serverCertificateBytes = serverCert.getEncoded();
        return this;
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) throws Exception {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsAssociate asdu = (CmsAssociate) request.getAsdu();

        if (!session.isNegotiated()) {
            log.warn("[Server] Association rejected: negotiation not completed");
            return buildNegativeResponse(request, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }

        // 1. Resolve serverAccessPointReference
        String sapRef = asdu.serverAccessPointReference() != null
            ? asdu.serverAccessPointReference().get() : null;
        if (sapRef == null || sapRef.isEmpty()) {
            if (sclDocument != null) {
                sapRef = sclDocument.getDefaultAccessPointReference();
                log.debug("[Server] Using default access point: {}", sapRef);
            }
            if (sapRef == null || sapRef.isEmpty()) {
                log.warn("[Server] No serverAccessPointReference and no default available");
                return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }
        }

        // 2. Validate against SCL model if loaded
        if (sclDocument != null) {
            String[] parts = sapRef.split("\\.");
            if (parts.length != 2) {
                log.warn("[Server] Invalid serverAccessPointReference format: {}", sapRef);
                return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
            }

            String iedName = parts[0];
            String apName = parts[1];

            SclIED ied = sclDocument.findIedByName(iedName);
            if (ied == null) {
                log.warn("[Server] IED not found in SCL model: {}", iedName);
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }

            SclIED.SclAccessPoint accessPoint = ied.findAccessPointByName(apName);
            if (accessPoint == null) {
                log.warn("[Server] AccessPoint not found in IED {}: {}", iedName, apName);
                return buildNegativeResponse(request, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }

            // Save AccessPoint info to session
            serverSession.setAccessPoint(sapRef, iedName, apName, accessPoint);
        }

        // 3. Validate authentication parameter if required
        AuthenticationParameter authParam = asdu.authenticationParameter();
        if (requireAuthentication && (authParam == null || authParam.signatureCertificate() == null)) {
            log.warn("[Server] Authentication required but no certificate provided");
            return buildNegativeResponse(request, CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }
        if (authenticator != null && authParam != null && authParam.signatureCertificate() != null) {
            byte[] signedData = prepareSignedData(asdu);

            Optional<CmsServiceError> authError = authenticator.validate(authParam, signedData);
            if (authError.isPresent()) {
                log.warn("[Server] Authentication failed: {}", authError.get());
                return buildNegativeResponse(request, authError.get().get());
            }
            log.debug("[Server] GM authentication successful for {}", sapRef);
        }

        // 4. Generate association ID
        byte[] assocId = AssociationIdGenerator.generate();
        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);

        // 5. Build positive response
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(assocId)
                .serviceError(CmsServiceError.NO_ERROR);

        // 6. Add server certificate in response for bidirectional auth
        if (serverCertificateBytes != null) {
            response.authenticationParameter(new AuthenticationParameter()
                .signatureCertificate(serverCertificateBytes));
        }

        log.debug("[Server] Association accepted, assocId={}, SAP={}",
                 hex(assocId), sapRef);
        return new CmsApdu(response);
    }

    @Override
    protected CmsApdu buildNegativeResponse(CmsApdu request, int errorCode) {
        CmsAssociate asdu = (CmsAssociate) request.getAsdu();
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_NEGATIVE)
                .reqId(asdu.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }

    private byte[] prepareSignedData(CmsAssociate asdu) {
        String sap = asdu.serverAccessPointReference().get();
        byte[] sapBytes = sap.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        if (asdu.authenticationParameter() != null &&
            asdu.authenticationParameter().signedTime() != null) {
            // Append timestamp bytes
            byte[] timeBytes = String.valueOf(asdu.authenticationParameter()
                .signedTime().secondsSinceEpoch.get()).getBytes();
            byte[] result = new byte[sapBytes.length + timeBytes.length];
            System.arraycopy(sapBytes, 0, result, 0, sapBytes.length);
            System.arraycopy(timeBytes, 0, result, sapBytes.length, timeBytes.length);
            return result;
        }

        return sapBytes;
    }

    private String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(bytes.length, 8); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        if (bytes.length > 8) sb.append("...");
        return sb.toString();
    }
}