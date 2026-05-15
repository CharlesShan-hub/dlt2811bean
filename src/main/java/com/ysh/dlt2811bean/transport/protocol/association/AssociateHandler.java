package com.ysh.dlt2811bean.transport.protocol.association;

import com.ysh.dlt2811bean.service.svc.association.datatypes.ServerAccessPointReference;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclAccessPoint;
import com.ysh.dlt2811bean.scl2.model.SclDocument;
import com.ysh.dlt2811bean.scl2.model.SclIED;
import com.ysh.dlt2811bean.security.GmAuthenticator;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.datatypes.AuthenticationParameter;
import com.ysh.dlt2811bean.transport.session.AssociationIdGenerator;
import com.ysh.dlt2811bean.transport.session.SessionState;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class AssociateHandler extends AbstractCmsServiceHandler<CmsAssociate> {

    private GmAuthenticator authenticator;
    private boolean requireAuthentication = false;
    private final SclDocument sclDocument;
    private byte[] serverCertificateBytes;
    private ServerAccessPointReference sapr;

    public AssociateHandler(SclDocument sclDocument) {
        super(ServiceName.ASSOCIATE, CmsAssociate::new, false);
        this.sclDocument = sclDocument;
    }

    public AssociateHandler enableSecurity(GmAuthenticator authenticator, X509Certificate serverCert) throws Exception {
        this.authenticator = authenticator;
        this.requireAuthentication = true;
        this.serverCertificateBytes = serverCert.getEncoded();
        return this;
    }

    @Override
    protected CmsApdu doServerHandle() throws Exception {

        // 1. Check session state
        if (!serverSession.isNegotiated()) {
            log.warn("[Server] Association rejected: negotiation not completed");
            return buildNegativeResponse(CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE);
        }

        // 2. Resolve serverAccessPointReference
        sapr = asdu.serverAccessPointReference();
        int error = validateSapRef();
        if (error != CmsServiceError.NO_ERROR) return buildNegativeResponse(error);

        // 3. Validate authentication parameter if required
        error = validateAuthParam();
        if (error != CmsServiceError.NO_ERROR) return buildNegativeResponse(error);

        // 4. Generate association ID
        byte[] assocId = AssociationIdGenerator.generate();
        serverSession.setAssociationId(assocId);
        serverSession.setState(SessionState.ASSOCIATED);

        // 5. Build positive response
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .associationId(assocId)
                .serviceError(CmsServiceError.NO_ERROR);

        // 6. Add server certificate in response for bidirectional auth
        if (serverCertificateBytes != null) 
            response.authenticationParameter(new AuthenticationParameter()
                .signatureCertificate(serverCertificateBytes));

        log.debug("[Server] Association accepted, assocId={}, SAP={}",
                 hex(assocId), sapr.get());
        return new CmsApdu(response);
    }

    private int validateSapRef(){

        if (sapr == null) {
            sapr = new ServerAccessPointReference();
        }
        if (sapr.get() == null || sapr.get().isEmpty()) {
            if (sclDocument != null) {
                sapr.set(sclDocument.getDefaultAccessPointReference());
                log.debug("[Server] Using default access point: {}", sapr.get());
            }
            if (sapr.get() == null || sapr.get().isEmpty()) {
                log.warn("[Server] No serverAccessPointReference and no default available");
                return CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
            }
        }

        if (sclDocument != null) {
            String iedName = sapr.getIedName();
            String apName = sapr.getAccessPoint();
            if (iedName.isEmpty() || apName.isEmpty()) {
                log.warn("[Server] Invalid serverAccessPointReference format: {}", sapr.get());
                return CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE;
            }

            SclIED ied = sclDocument.findIedByName(iedName);
            if (ied == null) {
                log.warn("[Server] IED not found in SCL model: {}", iedName);
                return CmsServiceError.INSTANCE_NOT_AVAILABLE;
            }

            SclAccessPoint accessPoint = ied.findAccessPointByName(apName);
            if (accessPoint == null) {
                log.warn("[Server] AccessPoint not found in IED {}: {}", iedName, apName);
                return CmsServiceError.INSTANCE_NOT_AVAILABLE;
            }

            // Save AccessPoint info to session
            serverSession.setAccessPoint(sapr.get(), iedName, apName, accessPoint);
            // Save DataTypeTemplates for type resolution
            serverSession.setSclDataTypeTemplates(sclDocument.getDataTypeTemplates());
        }

        return CmsServiceError.NO_ERROR;
    }

    private int validateAuthParam(){
        AuthenticationParameter authParam = asdu.authenticationParameter();
        if (requireAuthentication && (authParam == null || authParam.signatureCertificate() == null)) {
            log.warn("[Server] Authentication required but no certificate provided");
            return CmsServiceError.ACCESS_NOT_ALLOWED_IN_CURRENT_STATE;
        }
        if (authenticator != null && authParam != null && authParam.signatureCertificate() != null) {
            byte[] signedData = prepareSignedData();

            Optional<CmsServiceError> authError = authenticator.validate(authParam, signedData);

            if (authError.isPresent()) {
                log.warn("[Server] Authentication failed: {}", authError.get());
                return authError.get().get();
            }
            log.debug("[Server] GM authentication successful for {}", sapr.get());
        }
        return CmsServiceError.NO_ERROR;
    }

    @Override
    protected CmsApdu buildNegativeResponse(int errorCode) {
        CmsAssociate response = new CmsAssociate(MessageType.RESPONSE_NEGATIVE)
                .reqId(asdu.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }

    private byte[] prepareSignedData() {
        byte[] sapBytes = sapr.get().getBytes(java.nio.charset.StandardCharsets.UTF_8);

        if (asdu.authenticationParameter() != null &&
            asdu.authenticationParameter().signedTime() != null) {
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
