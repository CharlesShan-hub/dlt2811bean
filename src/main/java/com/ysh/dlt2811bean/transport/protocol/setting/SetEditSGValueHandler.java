package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.collection.CmsArray;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetEditSGValue;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class SetEditSGValueHandler extends AbstractCmsServiceHandler<CmsSetEditSGValue> {

    public SetEditSGValueHandler() {
        super(ServiceName.SET_EDIT_SG_VALUE, CmsSetEditSGValue::new);
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsSetEditSGValue asdu = (CmsSetEditSGValue) request.getAsdu();

        if (asdu.data == null || asdu.data.size() == 0) {
            return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        CmsArray<CmsServiceError> results = new CmsArray<>(CmsServiceError::new);
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

    @Override
    protected CmsApdu buildNegativeResponse(CmsApdu request, int errorCode) {
        return new CmsApdu(new CmsSetEditSGValue(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.getReqId())
                .addResult(errorCode));
    }
}
