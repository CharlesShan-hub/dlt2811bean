package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsOperate;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperateHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(OperateHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.OPERATE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling Operate: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsOperate) request.getAsdu(),
                    CmsAddCause.NOT_SUPPORTED);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsOperate asdu = (CmsOperate) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] Operate: empty reference");
            return buildNegativeResponse(asdu, CmsAddCause.NOT_SUPPORTED);
        }

        CmsOperate response = new CmsOperate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] Operate: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsOperate request, int addCauseCode) {
        CmsOperate response = new CmsOperate(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .reference(request.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(addCauseCode);
        return new CmsApdu(response);
    }
}
