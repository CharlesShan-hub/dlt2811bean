package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.app.node.InnerClient;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.svc.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.security.GmCredentialManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.ClientSession;
import com.ysh.jcms.utils.transport.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Signature;

/**
 * Client-side handler for Associate service.
 *
 * <p>Registered via {@link CmsNode#registerClient(Object)}.
 */
public class AssociateClient {

    private static final Logger log = LoggerFactory.getLogger(AssociateClient.class);
    private static final String SIGNATURE_ALGORITHM = "SM3withSM2";

    private final CmsNode node;
    private GmCredentialManager credentialManager;

    public AssociateClient(CmsNode node) {
        this.node = node;
    }

    public AssociateClient credentialManager(GmCredentialManager cm) {
        this.credentialManager = cm;
        return this;
    }

    private InnerClient client() { return node.getClient(); }
    private ClientSession session() { return node.getClient().getSession(); }

    public CmsAssociateResponse execute(AssociateClientDao dao) throws Exception {
        CmsAssociateRequest req = new CmsAssociateRequest();
        req.reqId(session().nextReqId());
        req.sapRef(dao.sapRef);
        req.sapRefPresent(dao.sapRef != null && !dao.sapRef.isEmpty());

        if (dao.secure && credentialManager != null) {
            req.authParam(buildAuthParam(dao.sapRef));
            req.authParamPresent(true);
        }

        byte[] reqBytes = req.encode();
        Frame response = client().sendRequest(ServiceName.ASSOCIATE, reqBytes);
        if (response == null) throw new IOException("Associate timeout");

        CmsAssociateResponse resp = new CmsAssociateResponse();
        try { resp.decode(response.asduBytes()); }
        catch (Exception e) { throw new IOException("Failed to decode AssociateResponse", e); }

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            session().setState(SessionState.DISCONNECTED);
            throw new IOException("Association rejected: error=" + serviceError);
        }

        byte[] assocId = resp.assocId.value();
        session().setAssociationId(assocId);
        session().setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", session().getSessionId());

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
