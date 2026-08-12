package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateError;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateRequest;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameCodec;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.Session;

public class NegotiateServer extends BaseServerHandler<CmsNegotiateRequest, CmsNegotiateError> {

    public NegotiateServer() {
        super(CmsServiceInfo.ASSOCIATE_NEGOTIATE, CmsNegotiateRequest.class, CmsNegotiateError.class);
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
        // DL/T 2811 8.15.2 c): reject if the peer's protocol version is unsupported
        if (req.protocolVersion.value() > config.protocolVersion())
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        return null;
    }

    private int calcNegotiatedApduSize(CmsNegotiateRequest req, CmsConfig.Protocol.Negotiate config) {
        // DL/T 2811 8.15.2 a): server returns the APDU size it can support
        return Math.min(req.apduSize.value(), config.apduSize());
    }

    private void applyNegotiation(Session session, CmsNegotiateRequest req, int negotiatedApduSize) {
        boolean fragSupported = negotiatedApduSize > req.asduSize.value(); // DL/T 2811 8.15.2 b): apduSize > asduSize means fragmentation
                                                                           // is supported
        session.connection().fragmentationSupported(fragSupported);

        session.negotiatedApduSize(negotiatedApduSize);
        session.negotiated(true);
        // Standard sizes: FL excludes APCH(4), payload excludes ReqID(2)
        session.connection().maxFrameSize(Math.max(0, negotiatedApduSize - FrameHeader.HEADER_SIZE));
        session.connection().peerAsduSize(Math.max(0, (int) req.asduSize.value() - FrameCodec.REQID_SIZE));
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
