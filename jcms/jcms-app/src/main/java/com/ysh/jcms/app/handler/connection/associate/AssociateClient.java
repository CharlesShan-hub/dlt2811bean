package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.svc.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.security.GmCredentialManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Signature;

/**
 * Client-side handler for Associate service.
 *
 * <p>Registered via {@link CmsNode#registerClient(Object)}.
 */
public class AssociateClient extends BaseClientHandler {

    private static final String SIGNATURE_ALGORITHM = "SM3withSM2";

    private GmCredentialManager credentialManager;

    public AssociateClient(CmsNode node) {
        super(node);
    }

    public AssociateClient credentialManager(GmCredentialManager cm) {
        this.credentialManager = cm;
        return this;
    }

    public CmsAssociateResponse execute(AssociateClientDao dao) throws Exception {
        CmsAssociateRequest req = new CmsAssociateRequest()
            .reqId(nextReqId())
            .sapRef(dao.sapRef())
            .sapRefPresent(dao.sapRef() != null && !dao.sapRef().isEmpty());

        if (dao.secure() && credentialManager != null) {
            req.authParam(buildAuthParam(dao.sapRef()));
            req.authParamPresent(true);
        }

        CmsAssociateResponse resp = decodeFrame(
            send(ServiceName.ASSOCIATE, req.encode()),
            new CmsAssociateResponse());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            node.getClient().getSession().setState(SessionState.DISCONNECTED);
            throw new IOException("Association rejected: error=" + serviceError);
        }

        node.getClient().getSession().setAssociationId(resp.assocId.value());
        node.getClient().getSession().setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", node.getClient().getSession().getSessionId());

        return resp;
    }

    private CmsAuthenticationParameter buildAuthParam(String sapRef) throws Exception {
        byte[] certBytes = credentialManager.getCertificate().getEncoded();
        byte[] sapBytes = sapRef.getBytes(StandardCharsets.UTF_8);
        long now = System.currentTimeMillis();
        byte[] timeBytes = String.valueOf(now).getBytes(StandardCharsets.UTF_8);

        byte[] signedData = new byte[sapBytes.length + timeBytes.length];
        System.arraycopy(sapBytes, 0, signedData, 0, sapBytes.length);
        System.arraycopy(timeBytes, 0, signedData, sapBytes.length, timeBytes.length);

        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");
        sig.initSign(credentialManager.getPrivateKey());
        sig.update(signedData);
        byte[] signatureValue = sig.sign();

        return new CmsAuthenticationParameter().cert(certBytes).sigVal(signatureValue);
    }
}
