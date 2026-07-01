package com.ysh.jcms.app.handler.report.report;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.report.CmsReport;
import com.ysh.jcms.svc.report.CmsReportDataEntry;
import com.ysh.jcms.utils.transport.frame.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * ReportClient — 客户端接收服务端推送的 REPORT 帧。
 *
 * <p>解码 CmsReport 并打印到控制台。
 */
public class ReportClient extends BaseClientHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportClient.class);

    public ReportClient(CmsNode node) { super(node); }

    /**
     * Handle an incoming unsolicited REPORT frame.
     * Called from the push listener.
     */
    public void handleReport(Frame frame) {
        try {
            CmsReport report = new CmsReport();
            report.entry.entryData.allocSize = 256;
            report.decode(frame.asduBytes());

            StringBuilder sb = new StringBuilder();
            sb.append("\n  ╔═══════════════════════════════════════════╗\n");
            sb.append("  ║        Server Report Received             ║\n");
            sb.append("  ╚═══════════════════════════════════════════╝\n");

            String rptID = report.rptID != null
                ? new String(report.rptID.value(), StandardCharsets.UTF_8)
                : "(null)";
            sb.append("    rptID: ").append(rptID).append("\n");

            if (report.sqNumPresent.value()) {
                sb.append("    sqNum: ").append(report.sqNum.value()).append("\n");
            }
            if (report.dataSetPresent.value()) {
                sb.append("    dataSet: ")
                  .append(new String(report.dataSet.value(), StandardCharsets.UTF_8))
                  .append("\n");
            }

            sb.append("    entryData (").append(report.entry.entryData.count).append(" items):\n");
            for (int i = 0; i < report.entry.entryData.count; i++) {
                CmsReportDataEntry ed = report.entry.entryData.items.get(i);
                sb.append("      [").append(i).append("] ");
                if (ed.refPresent.value()) {
                    sb.append(new String(ed.reference.value(), StandardCharsets.UTF_8));
                }
                if (ed.fcPresent.value()) {
                    sb.append(" fc=").append(ed.fc.value());
                }
                sb.append(" id=").append(ed.id.value());
                sb.append("\n");
            }

            String output = sb.toString();
            log.info("Report received:\n{}", output);
            System.out.println(output);

        } catch (Exception e) {
            log.error("Failed to decode report", e);
        }
    }
}
