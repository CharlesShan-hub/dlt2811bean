package com.ysh.dlt2811bean.transport.app.report;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

@DisplayName("Report Loopback Test")
class ReportLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("send report with rptID is accepted")
    void basicReport() throws Exception {
        associate();

        CmsReport asdu = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .rptID("PosReport");

        client.report(asdu);
    }
}
