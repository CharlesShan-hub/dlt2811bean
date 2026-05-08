package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperateTermination;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeActivatedOperateTerminationHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(TimeActivatedOperateTerminationHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling TimeActivatedOperateTermination: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsTimeActivatedOperateTermination) request.getAsdu(),
                    CmsAddCause.NOT_SUPPORTED);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsTimeActivatedOperateTermination asdu = (CmsTimeActivatedOperateTermination) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] TimeActivatedOperateTermination: empty reference");
            return buildNegativeResponse(asdu, CmsAddCause.NOT_SUPPORTED);
        }

        CmsTimeActivatedOperateTermination response = new CmsTimeActivatedOperateTermination(MessageType.REQUEST_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] TimeActivatedOperateTermination: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsTimeActivatedOperateTermination request, int addCauseCode) {
        CmsTimeActivatedOperateTermination response = new CmsTimeActivatedOperateTermination(MessageType.REQUEST_NEGATIVE)
                .reqId(request.reqId().get())
                .reference(request.reference.get())
                .ctlVal(new com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean(false))
                .addCause(addCauseCode);
        return new CmsApdu(response);
    }
}
