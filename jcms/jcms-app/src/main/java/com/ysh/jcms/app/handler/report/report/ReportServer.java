package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.core.pdu.report.CmsReport;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class ReportServer extends BaseServerHandler<CmsReport, CmsType> {

    public ReportServer() {
        super(CmsServiceInfo.REPORT, CmsReport.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsReport req, int reqId) {
        log.info("REPORT received from {}: rptID={}, entryData={}", session.sessionId(), str(req.rptID), req.entry.entryData.size());
        return null;
    }
}
