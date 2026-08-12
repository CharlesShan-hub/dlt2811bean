package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.BaseHandler;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateError;
import com.ysh.jcms.core.pdu.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.frame.FrameCodec;
import com.ysh.jcms.utils.transport.frame.FrameHeader;
import com.ysh.jcms.utils.transport.session.ClientSession;

import java.io.IOException;

public class NegotiateClient extends BaseClientHandler<NegotiateClientDao> {

    @Override
    public void execute(NegotiateClientDao dao) throws Exception {
        send(CmsServiceInfo.ASSOCIATE_NEGOTIATE, dao);
    }

    @Override
    protected void onSuccess(Frame frame, NegotiateClientDao dao) throws IOException {
        CmsNegotiateResponse resp = decodeResp(frame, new CmsNegotiateResponse());

        ClientSession session = node.client().session();
        session.negotiatedApduSize(resp.apduSize.value());
        session.negotiated(true);

        boolean fragSupported = resp.apduSize.value() > resp.asduSize.value(); // DL/T 2811 8.15.2 b)
        session.connection().fragmentationSupported(fragSupported);
        // Standard sizes: FL excludes APCH(4), payload excludes ReqID(2)
        session.connection().maxFrameSize(Math.max(0, resp.apduSize.value() - FrameHeader.HEADER_SIZE));
        session.connection().peerAsduSize(Math.max(0, (int) resp.asduSize.value() - FrameCodec.REQID_SIZE));

        // 存储响应结果
        BaseHandler.traceSession("Negotiated: apdu=" + resp.apduSize.value() + ", asdu=" + resp.asduSize.value() + ", version="
                + resp.protocolVersion.value());

        content().res(resp.inner.toJsonValue());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsNegotiateError err = decodeErr(frame, new CmsNegotiateError());
        throw new IOException("Negotiate rejected: " + err.value());
    }
}
