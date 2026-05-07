package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsAddCause;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsSelectWithValue;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectWithValueHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SelectWithValueHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SELECT_WITH_VALUE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SelectWithValue: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsSelectWithValue) request.getAsdu(),
                    CmsAddCause.SELECT_FAILED);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsSelectWithValue asdu = (CmsSelectWithValue) request.getAsdu();
        String ref = asdu.reference.get();

        if (ref == null || ref.isEmpty()) {
            log.warn("[Server] SelectWithValue: empty reference");
            return buildNegativeResponse(asdu, CmsAddCause.NOT_SUPPORTED);
        }

        CmsSelectWithValue response = new CmsSelectWithValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .reference(ref)
                .ctlVal(asdu.ctlVal.get())
                .ctlNum((int) asdu.ctlNum.get())
                .test(asdu.test.get());

        log.debug("[Server] SelectWithValue: {}", ref);
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsSelectWithValue request, int addCauseCode) {
        CmsSelectWithValue response = new CmsSelectWithValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .reference(request.reference.get())
                .addCause(addCauseCode);
        return new CmsApdu(response);
    }
}
