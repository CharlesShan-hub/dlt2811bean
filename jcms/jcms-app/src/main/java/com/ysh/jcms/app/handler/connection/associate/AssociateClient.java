package com.ysh.jcms.app.handler.connection.associate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.time.CmsUtcTime;
import com.ysh.jcms.svc.connection.CmsAssociateError;
import com.ysh.jcms.svc.connection.CmsAssociateRequest;
import com.ysh.jcms.svc.connection.CmsAssociateResponse;
import com.ysh.jcms.svc.connection.CmsAuthenticationParameter;
import com.ysh.jcms.utils.security.GmSignature;
import com.ysh.jcms.utils.security.GmCredentialManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.SessionState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AssociateClient extends BaseClientHandler {

    public void execute(AssociateClientDao dao) throws Exception {
        CmsAssociateRequest req = new CmsAssociateRequest().reqId(nextReqId()).sapRef(dao.sapRef())
                .sapRefPresent(dao.sapRef() != null && !dao.sapRef().isEmpty());

        if (dao.secure()) {
            req.authParam(buildAuthParam(node.getCredentialManager(), dao.sapRef()));
            req.authParamPresent(true);
        }

        send(ServiceName.ASSOCIATE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsAssociateError err = decodeErr(frame, new CmsAssociateError());
        node.getClient().getSession().setState(SessionState.DISCONNECTED);
        throw new IOException("Association rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsAssociateResponse resp = decodeResp(frame, new CmsAssociateResponse());

        int serviceError = resp.serviceError.value();
        if (serviceError != CmsServiceError.NO_ERROR) {
            node.getClient().getSession().setState(SessionState.DISCONNECTED);
            throw new IOException("Association rejected: " + new CmsServiceError(serviceError).constantName() + " (" + serviceError + ")");
        }

        node.getClient().getSession().setAssociationId(resp.assocId.value());
        node.getClient().getSession().setState(SessionState.ASSOCIATED);
        log.info("Association established: session={}", node.getClient().getSession().getSessionId());
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

        return new CmsAuthenticationParameter().cert(certBytes).signedTime(new CmsUtcTime().now()).sigVal(signatureValue);
    }
}
