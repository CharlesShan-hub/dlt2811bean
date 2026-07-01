package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.svc.report.CmsReport;
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
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsReport report = (CmsReport) rawReq;
        log.info("REPORT received from {}: rptID={}, entryData={}",
            session.getSessionId(), str(report.rptID.value()), report.entry.entryData.count);
        return null;
    }
}
