package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

/**
 * Handler for Report service (SC=0x5A).
 */
public class ReportHandler implements CmsServiceHandler {

    @Override
    public ServiceName getServiceName() {
        return ServiceName.REPORT;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        CmsReport asdu = (CmsReport) request.getAsdu();
        return new CmsApdu(asdu);
    }
}
