package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateError;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateRequest;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class NegotiateServer extends BaseServerHandler {

    public NegotiateServer() {
        super(ServiceName.ASSOCIATE_NEGOTIATE, CmsNegotiateRequest.class, CmsNegotiateError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsNegotiateRequest req = (CmsNegotiateRequest) rawReq;
        CmsConfig.Protocol.Negotiate config = CmsConfigLoader.load().getProtocol().getNegotiate();

        if (req.protocolVersion.value() > config.getProtocolVersion())
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        int negotiatedApduSize = Math.min(req.apduSize.value(), config.getApduSize());

        // 标准 b)：apduSize > asduSize → 支持分帧
        boolean fragSupported = negotiatedApduSize > req.asduSize.value();
        session.setFragmentationSupported(fragSupported);
        session.getConnection().setFragmentationSupported(fragSupported);

        session.setNegotiatedApduSize(negotiatedApduSize);
        session.setPeerAsduSize((int) req.asduSize.value());
        session.setPeerProtocolVersion((int) req.protocolVersion.value());
        session.setNegotiated(true);
        session.getConnection().setMaxFrameSize(negotiatedApduSize);
        session.getConnection().setPeerAsduSize((int) req.asduSize.value());

        log.info("Negotiate completed: apduSize={}, asduSize={}, protocolVersion={}, modelVersion={}", negotiatedApduSize,
                config.getAsduSize(), config.getProtocolVersion(), config.getModelVersion());

        return buildSuccess(new CmsNegotiateResponse().reqId(reqId).apduSize(negotiatedApduSize).asduSize(config.getAsduSize())
                .protocolVersion(config.getProtocolVersion())
                .modelVersion(config.getModelVersion().getBytes(java.nio.charset.StandardCharsets.UTF_8)).encode(), reqId);
    }
}
