package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsConfirmEditSGValues;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class ConfirmEditSGValuesHandler extends AbstractCmsServiceHandler<CmsConfirmEditSGValues> {

    public ConfirmEditSGValuesHandler() {
        super(ServiceName.CONFIRM_EDIT_SG_VALUES, CmsConfirmEditSGValues::new);
       }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsConfirmEditSGValues asdu = (CmsConfirmEditSGValues) request.getAsdu();

        String ref = asdu.sgcbReference.get();
        if (ref == null || ref.isEmpty()) {
            return buildNegativeResponse(request, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        log.debug("[Server] ConfirmEditSGValues: ref={}", ref);

        CmsConfirmEditSGValues response = new CmsConfirmEditSGValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        return new CmsApdu(response);
    }
}
