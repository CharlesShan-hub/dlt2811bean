package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.transport.app.ReportListener;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;

import java.util.List;

public class ReportHandler extends AbstractCmsServiceHandler<CmsReport> {

    private final List<ReportListener> listeners;

    public ReportHandler() {
        this(null);
    }

    public ReportHandler(List<ReportListener> listeners) {
        super(ServiceName.REPORT, CmsReport::new);
        this.listeners = listeners;
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) throws Exception {
        CmsReport report = (CmsReport) request.getAsdu();

        String rptID = report.rptID.get();
        if (rptID == null || rptID.isEmpty()) {
            log.warn("[Client] Report: empty rptID");
            return null;
        }

        int entryDataCount = report.entry.entryData.size();
        log.info("[Client] Report: rptID={}, entryData={}", rptID, entryDataCount);

        if (listeners != null) {
            for (ReportListener listener : listeners) {
                try {
                    listener.onReportReceived(report);
                } catch (Exception e) {
                    log.warn("ReportListener error: {}", e.getMessage(), e);
                }
            }
        }

        CmsReport response = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .reqId(report.reqId().get());
        log.debug("[Client] Report acknowledged: {}", rptID);
        return new CmsApdu(response);
    }

    @Override
    protected CmsApdu doServerHandle() {
        String rptID = asdu.rptID.get();
        if (rptID == null || rptID.isEmpty()) {
            log.warn("[Server] Report from client: empty rptID");
            return null;
        }

        int entryDataCount = asdu.entry.entryData.size();
        log.info("[Server] Report from client: rptID={}, entryData={}", rptID, entryDataCount);

        CmsReport response = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Server] Report acknowledged: {}", rptID);
        return new CmsApdu(response);
    }
}
