package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class ReportHandler extends AbstractCmsServiceHandler<CmsReport> {

    public ReportHandler() {
        super(ServiceName.REPORT, CmsReport::new);
    }

    @Override
    protected CmsApdu doServerHandle() {

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
