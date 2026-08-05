package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.pdu.report.CmsReport;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class ReportServer extends BaseServerHandler {


    public ReportServer() {
        super(ServiceName.REPORT, CmsReport.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsReport report = (CmsReport) rawReq;
        log.info("REPORT received from {}: rptID={}, entryData={}", session.getSessionId(), str(report.rptID),
                report.entry.entryData.size());
        return null;
    }
}
