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
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.AssociationIdGenerator;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.session.SessionState;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class AssociateHandler implements CmsServiceHandler {

    private GmAuthenticator authenticator;
    private boolean requireAuthentication = false;
    private final SclDocument sclDocument;
    private byte[] serverCertificateBytes;

    public AssociateHandler() {
        this.sclDocument = null;
    }

    public AssociateHandler(SclDocument sclDocument) {
        this.sclDocument = sclDocument;
    }

    public AssociateHandler enableSecurity(GmAuthenticator authenticator, X509Certificate serverCert) throws Exception {
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
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsAssociate asdu = (CmsAssociate) request.getAsdu();

        // 1. Resolve serverAccessPointReference
        String sapRef = asdu.serverAccessPointReference() != null
            ? asdu.serverAccessPointReference().get() : null;
        if (sapRef == null || sapRef.isEmpty()) {
            if (sclDocument != null) {
                sapRef = sclDocument.getDefaultAccessPointReference();
                log.info("[Server] Using default access point: {}", sapRef);
            }
            if (sapRef == null || sapRef.isEmpty()) {
                log.warn("[Server] No serverAccessPointReference and no default available");
                return buildNegativeResponse(asdu, new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }
        }

        // 2. Validate against SCL model if loaded
        if (sclDocument != null) {
            String[] parts = sapRef.split("\\.");
            if (parts.length != 2) {
                log.warn("[Server] Invalid serverAccessPointReference format: {}", sapRef);
                return buildNegativeResponse(asdu, new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
            }

            String iedName = parts[0];
            String apName = parts[1];

            SclIED ied = sclDocument.findIedByName(iedName);
            if (ied == null) {
                log.warn("[Server] IED not found in SCL model: {}", iedName);
                return buildNegativeResponse(asdu, new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE));
            }

            SclIED.SclAccessPoint accessPoint = ied.findAccessPointByName(apName);
            if (accessPoint == null) {
                log.warn("[Server] AccessPoint not found in IED {}: {}", iedName, apName);
                return buildNegativeResponse(asdu, new CmsServiceError(CmsServiceError.INSTANCE_NOT_AVAILABLE));
            }

            // Save AccessPoint info to session
            session.setAccessPoint(sapRef, iedName, apName, accessPoint);
        }

        // 3. Validate authentication parameter if required
        AuthenticationParameter authParam = asdu.authenticationParameter();
        if (requireAuthentication && (authParam == null || authParam.signatureCertificate() == null)) {
            log.warn("[Server] Authentication required but no certificate provided");
            return buildNegativeResponse(asdu, new CmsServiceError(CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE));
        }
        if (authenticator != null && authParam != null && authParam.signatureCertificate() != null) {
            byte[] signedData = prepareSignedData(asdu);

            Optional<CmsServiceError> authError = authenticator.validate(authParam, signedData);
            if (authError.isPresent()) {
                log.warn("[Server] Authentication failed: {}", authError.get());
                return buildNegativeResponse(asdu, authError.get());
            }
            log.info("[Server] GM authentication successful for {}", sapRef);
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

        log.info("[Server] Association accepted, assocId={}, SAP={}",
                 hex(assocId), sapRef);
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

    private CmsApdu buildNegativeResponse(CmsAssociate request, CmsServiceError error) {
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(error.get());
        return new CmsApdu(response);
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