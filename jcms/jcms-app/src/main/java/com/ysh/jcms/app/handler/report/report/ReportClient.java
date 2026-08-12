package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.report.CmsReport;
import com.ysh.jcms.utils.transport.frame.Frame;

/**
 * ReportClient — 客户端接收服务端推送的 REPORT 帧。
 */
public class ReportClient extends BaseClientHandler<ReportDao> {

    @Override
    public void execute(ReportDao dao) throws Exception {
        // ReportClient is a server-push handler; no direct execute.
    }

    public void handleReport(Frame frame) {
        try {
            CmsReport report = new CmsReport();
            report.decode(frame.asduBytes());

            StringBuilder sb = new StringBuilder();
            String rptID = report.rptID != null ? report.rptID.value() : "(null)";
            sb.append("\n  Report Received: rptID=").append(rptID);
            if (report.isPresent("sqNum")) {
                sb.append(" sqNum=").append(report.sqNum.value());
            }
            if (report.isPresent("dataSet")) {
                sb.append(" dataSet=").append(report.dataSet.value());
            }
            sb.append(" entries=").append(report.entry.entryData.size());

            String output = sb.toString();
            log.info("Report received: {}", output);

        } catch (Exception e) {
            log.error("Failed to decode/handle report", e);
        }
    }
}
