package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;

public class ReportHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.REPORT;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling Report: {}", e.getMessage(), e);
            return null;
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsReport asdu = (CmsReport) request.getAsdu();

        String rptID = asdu.rptID.get();
        if (rptID == null || rptID.isEmpty()) {
            log.warn("[Client] Report: empty rptID");
            return null;
        }

        int entryDataCount = asdu.entry.entryData.size();
        log.info("[Client] Report: rptID={}, entryData={}", rptID, entryDataCount);

        CmsReport response = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        log.debug("[Client] Report acknowledged: {}", rptID);
        return new CmsApdu(response);
    }
}
