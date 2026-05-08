package com.ysh.dlt2811bean.transport.app.report;

import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32U;
import com.ysh.dlt2811bean.datatypes.string.CmsObjectReference;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.report.CmsReport;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsReportEntryData;
import com.ysh.dlt2811bean.transport.app.LoopbackTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Report Loopback Test")
class ReportLoopbackTest extends LoopbackTest {

    @Test
    @DisplayName("send report with rptID is accepted")
    void basicReport() throws Exception {
        associate();

        CmsReport asdu = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .rptID("PosReport");

        CmsApdu response = client.report(asdu);
        //System.out.println(response);

        assertNotNull(response);
    }
}
