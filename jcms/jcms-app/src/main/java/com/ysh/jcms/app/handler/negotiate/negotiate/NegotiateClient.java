package com.ysh.jcms.app.handler.negotiate.negotiate;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateError;
import com.ysh.jcms.pdu.negotiate.CmsNegotiateResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.ClientSession;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class NegotiateClient extends BaseClientHandler<NegotiateClientDao> {

    @Override
    public void execute(NegotiateClientDao dao) throws Exception {
        send(ServiceName.ASSOCIATE_NEGOTIATE, dao);
    }

    @Override
    protected void onSuccess(Frame frame, NegotiateClientDao dao) throws IOException {
        CmsNegotiateResponse resp = decodeResp(frame, new CmsNegotiateResponse());

        ClientSession session = node.client().session();
        session.negotiatedApduSize(resp.apduSize.value());
        session.peerAsduSize((int) resp.asduSize.value());
        session.peerProtocolVersion((int) resp.protocolVersion.value());
        session.negotiated(true);

        // 标准 b)：apduSize > asduSize → 支持分帧
        boolean fragSupported = resp.apduSize.value() > resp.asduSize.value();
        session.fragmentationSupported(fragSupported);
        session.connection().fragmentationSupported(fragSupported);
        session.connection().maxFrameSize(resp.apduSize.value());
        session.connection().peerAsduSize((int) resp.asduSize.value());

        // 存储响应结果
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("apduSize", resp.apduSize.value());
        resultMap.put("asduSize", resp.asduSize.value());
        resultMap.put("protocolVersion", resp.protocolVersion.value());
        resultMap.put("modelVersion", resp.modelVersion.value());
        content().res(Collections.singletonList(resultMap));
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsNegotiateError err = decodeErr(frame, new CmsNegotiateError());
        throw new IOException("Negotiate rejected: " + err.value());
    }
}
