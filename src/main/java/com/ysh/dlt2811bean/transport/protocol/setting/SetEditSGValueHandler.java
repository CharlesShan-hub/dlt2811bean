package com.ysh.dlt2811bean.transport.protocol.setting;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.setting.CmsSetEditSGValue;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for SetEditSGValue service (SC=0x56).
 */
public class SetEditSGValueHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_EDIT_SG_VALUE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsSetEditSGValue asdu = (CmsSetEditSGValue) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
