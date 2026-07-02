package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.negotiate.CmsNegotiateError;
import com.ysh.jcms.svc.negotiate.CmsNegotiateRequest;
import com.ysh.jcms.svc.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.ClientSession;

import java.io.IOException;

public class NegotiateClient extends BaseClientHandler {

    public NegotiateClient(CmsNode node) {
        super(node);
    }

    public void execute(NegotiateClientDao dao) throws Exception {
        CmsNegotiateRequest req = new CmsNegotiateRequest()
            .reqId(nextReqId())
            .apduSize(dao.apduSize())
            .asduSize(dao.asduSize())
            .protocolVersion(dao.protocolVersion());

        send(ServiceName.ASSOCIATE_NEGOTIATE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsNegotiateError err = new CmsNegotiateError();
        err.decode(frame.asduBytes());
        throw new IOException("Negotiate rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsNegotiateResponse resp = new CmsNegotiateResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        ClientSession session = node.getClient().getSession();
        session.setNegotiatedApduSize(resp.apduSize.value());
        session.setPeerAsduSize((int) resp.asduSize.value());
        session.setPeerProtocolVersion((int) resp.protocolVersion.value());
        session.setNegotiated(true);
        session.getConnection().setMaxFrameSize(resp.apduSize.value());

        log.info("Negotiate completed: apduSize={}, asduSize={}, protocolVersion={}, modelVersion={}",
            resp.apduSize.value(), resp.asduSize.value(), resp.protocolVersion.value(),
            new String(resp.modelVersion.value()));
    }
}
