package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateError;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateRequest;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class NegotiateServer extends BaseServerHandler<CmsNegotiateRequest, CmsNegotiateError> {

    public NegotiateServer() {
        super(ServiceName.ASSOCIATE_NEGOTIATE, CmsNegotiateRequest.class, CmsNegotiateError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsNegotiateRequest req, int reqId) {
        CmsConfig.Protocol.Negotiate config = CmsConfigLoader.load().protocol().negotiate();

        Frame err = validateProtocolVersion(req, config, reqId);
        if (err != null)
            return err;

        int negotiatedApduSize = calcNegotiatedApduSize(req, config);
        applyNegotiation(session, req, negotiatedApduSize);
        logNegotiation(negotiatedApduSize, config);

        return buildResponse(negotiatedApduSize, config, reqId);
    }

    private Frame validateProtocolVersion(CmsNegotiateRequest req, CmsConfig.Protocol.Negotiate config, int reqId) {
        if (req.protocolVersion.value() > config.protocolVersion())
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        return null;
    }

    private int calcNegotiatedApduSize(CmsNegotiateRequest req, CmsConfig.Protocol.Negotiate config) {
        return Math.min(req.apduSize.value(), config.apduSize());
    }

    private void applyNegotiation(Session session, CmsNegotiateRequest req, int negotiatedApduSize) {
        boolean fragSupported = negotiatedApduSize > req.asduSize.value();
        session.setFragmentationSupported(fragSupported);
        session.getConnection().setFragmentationSupported(fragSupported);

        session.setNegotiatedApduSize(negotiatedApduSize);
        session.setPeerAsduSize((int) req.asduSize.value());
        session.setPeerProtocolVersion((int) req.protocolVersion.value());
        session.setNegotiated(true);
        session.getConnection().setMaxFrameSize(negotiatedApduSize);
        session.getConnection().setPeerAsduSize((int) req.asduSize.value());
    }

    private void logNegotiation(int negotiatedApduSize, CmsConfig.Protocol.Negotiate config) {
        log.info("Negotiate completed: apduSize={}, asduSize={}, protocolVersion={}, modelVersion={}", negotiatedApduSize,
                config.asduSize(), config.protocolVersion(), config.modelVersion());
    }

    private Frame buildResponse(int negotiatedApduSize, CmsConfig.Protocol.Negotiate config, int reqId) {
        return buildSuccess(new CmsNegotiateResponse().apduSize(negotiatedApduSize).asduSize(config.asduSize())
                .protocolVersion(config.protocolVersion()).modelVersion(config.modelVersion()).encode(), reqId);
    }
}
