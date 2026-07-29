package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.pdu.report.CmsReport;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportServer.class);

    public ReportServer() {
        super(ServiceName.REPORT, CmsReport.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsReport report = (CmsReport) rawReq;
        log.info("REPORT received from {}: rptID={}, entryData={}", session.getSessionId(), str(report.rptID.value()),
                report.entry.entryData.count);
        return null;
    }
}
