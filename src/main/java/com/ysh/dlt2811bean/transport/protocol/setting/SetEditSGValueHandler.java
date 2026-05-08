package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetEditSGValue;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetEditSGValueHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetEditSGValueHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_EDIT_SG_VALUE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetEditSGValue: {}", e.getMessage(), e);
            int reqId = request != null ? ((CmsSetEditSGValue) request.getAsdu()).reqId().get() : 0;
            return buildNegativeResponse(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsSetEditSGValue asdu = (CmsSetEditSGValue) request.getAsdu();

        if (asdu.data == null || asdu.data.size() == 0) {
            return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new).capacity(100);
        boolean hasAnyError = false;

        for (int i = 0; i < asdu.data.size(); i++) {
            String ref = asdu.data.get(i).reference.get();
            if (ref == null || ref.isEmpty()) {
                results.add(new CmsServiceError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE));
                hasAnyError = true;
            } else {
                results.add(new CmsServiceError(CmsServiceError.NO_ERROR));
            }
        }

        if (hasAnyError) {
            CmsSetEditSGValue response = new CmsSetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                    .reqId(asdu.reqId().get());
            response.result = results;
            return new CmsApdu(response);
        }

        return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private CmsApdu buildNegativeResponse(int reqId, int errorCode) {
        return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(reqId)
                .addResult(errorCode));
    }
}
