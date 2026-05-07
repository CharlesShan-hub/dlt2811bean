package com.ysh.dlt2811bean.transport.protocol.control;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.control.CmsTimeActivatedOperateTermination;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for TimeActivatedOperateTermination service (SC=0x4A).
 */
public class TimeActivatedOperateTerminationHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.TIME_ACTIVATED_OPERATE_TERMINATION;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsTimeActivatedOperateTermination asdu = (CmsTimeActivatedOperateTermination) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
