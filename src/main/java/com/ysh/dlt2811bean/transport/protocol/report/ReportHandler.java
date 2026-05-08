package com.ysh.dlt2811bean.transport.protocol.report;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsReportEntry;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(ReportHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.REPORT;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling Report: {}", e.getMessage(), e);
            return null;
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsReport asdu = (CmsReport) request.getAsdu();

        String rptID = asdu.rptID.get();
        if (rptID == null || rptID.isEmpty()) {
            log.warn("[Server] Report: empty rptID");
            return null;
        }

        int entryDataCount = asdu.entry.entryData.size();
        log.info("[Server] Report: rptID={}, entryData={}", rptID, entryDataCount);

        return new CmsApdu(asdu);
    }
}
