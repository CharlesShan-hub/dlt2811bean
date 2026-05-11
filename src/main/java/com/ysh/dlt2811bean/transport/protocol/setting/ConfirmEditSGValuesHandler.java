package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsConfirmEditSGValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class ConfirmEditSGValuesHandler extends AbstractCmsServiceHandler<CmsConfirmEditSGValues> {

    public ConfirmEditSGValuesHandler() {
        super(ServiceName.CONFIRM_EDIT_SG_VALUES, CmsConfirmEditSGValues::new);
       }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling ConfirmEditSGValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsConfirmEditSGValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsConfirmEditSGValues asdu = (CmsConfirmEditSGValues) request.getAsdu();

        String ref = asdu.sgcbReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        log.debug("[Server] ConfirmEditSGValues: ref={}", ref);

        CmsConfirmEditSGValues response = new CmsConfirmEditSGValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }

    private CmsApdu buildNegativeResponse(CmsConfirmEditSGValues request, int errorCode) {
        CmsConfirmEditSGValues response = new CmsConfirmEditSGValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
