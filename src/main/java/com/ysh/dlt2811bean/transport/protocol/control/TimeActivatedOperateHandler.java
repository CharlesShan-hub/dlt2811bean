package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperate;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeActivatedOperateHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(TimeActivatedOperateHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TIME_ACTIVATED_OPERATE;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling TimeActivatedOperate: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsTimeActivatedOperate) request.getAsdu(),
                    CmsAddCause.NOT_SUPPORTED);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsTimeActivatedOperate asdu = (CmsTimeActivatedOperate) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] TimeActivatedOperate: empty reference");
            return buildNegativeResponse(asdu, CmsAddCause.NOT_SUPPORTED);
        }

        CmsTimeActivatedOperate response = new CmsTimeActivatedOperate(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] TimeActivatedOperate: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsTimeActivatedOperate request, int addCauseCode) {
        CmsTimeActivatedOperate response = new CmsTimeActivatedOperate(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .reference(request.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(addCauseCode);
        return new CmsApdu(response);
    }
}
