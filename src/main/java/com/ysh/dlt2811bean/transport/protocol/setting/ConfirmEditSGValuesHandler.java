package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsConfirmEditSGValues;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for ConfirmEditSGValues service (SC=0x57).
 */
public class ConfirmEditSGValuesHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.CONFIRM_EDIT_SG_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsConfirmEditSGValues asdu = (CmsConfirmEditSGValues) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
