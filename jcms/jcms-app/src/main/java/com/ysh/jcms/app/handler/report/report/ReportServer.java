package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsReport;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ReportServer — 服务端接收 REPORT 帧。
 *
 * <p>REPORT 是 Unconfirmed 服务（服务端主动推送），但客户端也可以发 Report 给服务端。
 * 这个 handler 仅用于日志记录，不做特殊处理。
 */
public class ReportServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportServer.class);

    public ReportServer() {
        super(ServiceName.REPORT, CmsReport.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsReport report = (CmsReport) rawReq;
        log.info("REPORT received from {}: rptID={}, entryData={}",
            session.getSessionId(),
            report.rptID != null ? new String(report.rptID.value(), java.nio.charset.StandardCharsets.UTF_8) : "(null)",
            report.entry.entryData.count);

        // Report is unconfirmed — no response
        return null;
    }
}
