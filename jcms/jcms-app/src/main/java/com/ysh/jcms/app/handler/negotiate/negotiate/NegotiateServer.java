package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.negotiate.CmsNegotiateError;
import com.ysh.jcms.svc.negotiate.CmsNegotiateRequest;
import com.ysh.jcms.svc.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;

public class NegotiateServer extends BaseServerHandler {

    public NegotiateServer() {
        super(ServiceName.ASSOCIATE_NEGOTIATE);
    }

    @Override
    public Frame handleRequest(Session session, Frame request) {
        CmsNegotiateRequest req = new CmsNegotiateRequest();
        if (!tryDecode(session, request, req)) {
            return buildError(new CmsNegotiateError()
                .reqId(0)
                .serviceError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT)
                .encode(), 0);
        }

        int reqId = req.reqId.value();
        CmsConfig.Negotiate config = CmsConfigLoader.load().getNegotiate();

        long clientProtocolVersion = req.protocolVersion.value();
        if (clientProtocolVersion > config.getProtocolVersion()) {
            log.warn("Negotiate rejected: client protocolVersion={} > server protocolVersion={}",
                clientProtocolVersion, config.getProtocolVersion());
            return buildError(new CmsNegotiateError()
                .reqId(reqId)
                .serviceError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT)
                .encode(), reqId);
        }

        int negotiatedApduSize = Math.min(req.apduSize.value(), config.getApduSize());

        session.setNegotiatedApduSize(negotiatedApduSize);
        session.setPeerAsduSize((int) req.asduSize.value());
        session.setPeerProtocolVersion((int) clientProtocolVersion);
        session.setNegotiated(true);

        session.getConnection().setMaxFrameSize(negotiatedApduSize);

        byte[] modelBytes = config.getModelVersion().getBytes(StandardCharsets.UTF_8);

        byte[] respBytes = new CmsNegotiateResponse()
            .reqId(reqId)
            .apduSize(negotiatedApduSize)
            .asduSize(config.getAsduSize())
            .protocolVersion(config.getProtocolVersion())
            .modelVersion(modelBytes)
            .encode();

        log.info("Negotiate completed: apduSize={}, asduSize={}, protocolVersion={}, modelVersion={}",
            negotiatedApduSize, config.getAsduSize(), config.getProtocolVersion(), config.getModelVersion());

        return buildSuccess(respBytes, reqId);
    }
}