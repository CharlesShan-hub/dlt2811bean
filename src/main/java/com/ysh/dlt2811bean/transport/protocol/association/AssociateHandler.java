package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
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

    private final GmAuthenticator authenticator;

    public AssociateHandler() {
        this.authenticator = null; // No authentication by default
    }

    public AssociateHandler(GmAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.ASSOCIATE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsAssociate asdu = (CmsAssociate) request.getAsdu();

        // 1. Validate serverAccessPointReference
        if (asdu.serverAccessPointReference() == null ||
            asdu.serverAccessPointReference().get() == null ||
            asdu.serverAccessPointReference().get().isEmpty()) {
            log.warn("[Server] Invalid serverAccessPointReference");
            return buildNegativeResponse(asdu, new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
        }

        // 2. Validate authentication parameter if required
        AuthenticationParameter authParam = asdu.authenticationParameter();
        if (authenticator != null && authParam != null && authParam.signatureCertificate() != null) {
            // Prepare signed data: serverAccessPointReference + other request fields
            byte[] signedData = prepareSignedData(asdu);

            Optional<CmsServiceError> authError = authenticator.validate(authParam, signedData);
            if (authError.isPresent()) {
                log.warn("[Server] Authentication failed: {}", authError.get());
                return buildNegativeResponse(asdu, authError.get());
            }
            log.info("[Server] GM authentication successful for {}", asdu.serverAccessPointReference());
        }

        // 3. Generate association ID
        byte[] assocId = AssociationIdGenerator.generate();
        session.setAssociationId(assocId);
        session.setState(SessionState.ASSOCIATED);

        // 4. Build positive response
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(assocId)
                .serviceError(CmsServiceError.NO_ERROR);

        // Add authentication parameter in response if requested
        if (authParam != null) {
            response.authenticationParameter(authParam);
        }

        log.info("[Server] Association accepted, assocId={}, SAP={}",
                 hex(assocId), asdu.serverAccessPointReference());
        return new CmsApdu(response);
    }

    /**
     * Prepare data for signature verification.
     *
     * <p>According to DL/T 2811-2024, the signed data includes:
     * serverAccessPointReference concatenated with request timestamp.
     */
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

    /**
     * Build negative (error) response.
     */
    private CmsApdu buildNegativeResponse(CmsAssociate request, CmsServiceError error) {
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(error.get()); // CmsServiceError.get() returns int
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