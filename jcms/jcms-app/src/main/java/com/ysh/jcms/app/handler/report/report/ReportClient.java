package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.report.CmsReport;
import com.ysh.jcms.utils.transport.frame.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * ReportClient — 客户端接收服务端推送的 REPORT 帧。
 */
public class ReportClient extends BaseClientHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportClient.class);

    public ReportClient(CmsNode node) {
        super(node);
    }

    public void handleReport(Frame frame) {
        try {
            CmsReport report = new CmsReport();
            report.decode(frame.asduBytes());

            StringBuilder sb = new StringBuilder();
            String rptID = report.rptID != null ? new String(report.rptID.value(), StandardCharsets.UTF_8) : "(null)";
            sb.append("\n  Report Received: rptID=").append(rptID);
            if (report.sqNumPresent.value()) {
                sb.append(" sqNum=").append(report.sqNum.value());
            }
            if (report.dataSetPresent.value()) {
                sb.append(" dataSet=").append(new String(report.dataSet.value(), StandardCharsets.UTF_8));
            }
            sb.append(" entries=").append(report.entry.entryData.count);

            String output = sb.toString();
            log.info("Report received: {}", output);
            System.out.println(output);

        } catch (Exception e) {
            log.error("Failed to decode/handle report", e);
        }
    }
}
