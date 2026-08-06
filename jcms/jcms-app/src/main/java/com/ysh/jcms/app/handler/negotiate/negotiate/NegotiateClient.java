package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateError;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.ClientSession;

import java.io.IOException;

public class NegotiateClient extends BaseClientHandler<NegotiateClientDao> {

    @Override
    public void execute(NegotiateClientDao dao) throws Exception {
        send(ServiceName.ASSOCIATE_NEGOTIATE, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsNegotiateError err = decodeErr(frame, new CmsNegotiateError());
        throw new IOException("Negotiate rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsNegotiateResponse resp = decodeResp(frame, new CmsNegotiateResponse());

        ClientSession session = node.getClient().getSession();
        session.setNegotiatedApduSize(resp.apduSize.value());
        session.setPeerAsduSize((int) resp.asduSize.value());
        session.setPeerProtocolVersion((int) resp.protocolVersion.value());
        session.setNegotiated(true);

        // 标准 b)：apduSize > asduSize → 支持分帧
        boolean fragSupported = resp.apduSize.value() > resp.asduSize.value();
        session.setFragmentationSupported(fragSupported);
        session.getConnection().setFragmentationSupported(fragSupported);
        session.getConnection().setMaxFrameSize(resp.apduSize.value());
        session.getConnection().setPeerAsduSize((int) resp.asduSize.value());

        log.info("Negotiate completed: apduSize={}, asduSize={}, protocolVersion={}, modelVersion={}", resp.apduSize.value(),
                resp.asduSize.value(), resp.protocolVersion.value(), resp.modelVersion.value());
    }
}
