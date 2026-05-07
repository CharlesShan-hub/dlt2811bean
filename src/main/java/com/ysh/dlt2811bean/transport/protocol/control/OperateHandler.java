package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsOperate;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for Operate service (SC=0x47).
 */
public class OperateHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.OPERATE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsOperate asdu = (CmsOperate) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
