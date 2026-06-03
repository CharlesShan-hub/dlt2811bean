package com.ysh.dlt2811bean.service.svc.report;

import com.ysh.dlt2811bean.datatypes.code.CmsRcbOptFlds;
import com.ysh.dlt2811bean.datatypes.code.CmsReasonCode;
import com.ysh.dlt2811bean.datatypes.numeric.CmsInt32;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.svc.report.datatypes.CmsReportEntryData;
import com.ysh.dlt2811bean.service.testutil.AsduTestUtil;
import com.ysh.dlt2811bean.service.testutil.mixin.CopyTest;
import com.ysh.dlt2811bean.service.testutil.mixin.ServiceNameTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsReport")
class CmsReportTest implements ServiceNameTest<CmsReport>, CopyTest<CmsReport> {

    @Override
    public ServiceName expectedServiceName() { return ServiceName.REPORT; }

    @Override
    public CmsReport createAsdu() { return new CmsReport(MessageType.RESPONSE_POSITIVE); }

    @Override
    public CmsReport createCopyableAsdu() { return createSampleReport(); }

    private CmsReport createSampleReport() {
        CmsReport report = new CmsReport(MessageType.RESPONSE_POSITIVE)
                .rptID("IED1.AP1.LD1.LN1.RPT1").sqNum(42).subSqNum(1)
                .moreSegmentsFollow(false).datSet("IED1.AP1.LD1.LN1.DS1")
                .bufOvfl(false).confRev(1).reqId(1);
        report.optFlds.setBit(CmsRcbOptFlds.SEQUENCE_NUMBER, true);
        report.optFlds.setBit(CmsRcbOptFlds.REPORT_TIME_STAMP, true);
        report.entry.timeOfEntry.msOfDay(43200000L).daysSince1984(15000);
        report.entry.entryID.set(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07});
        CmsReportEntryData data1 = new CmsReportEntryData();
        data1.reference.set("IED1.AP1.LD1.LN1.DO1");
        data1.fc.set("ST");
        data1.id.set(1);
        data1.value(new CmsInt32(100));
        data1.reason.setBit(CmsReasonCode.DATA_CHANGE, true);
        report.entry.entryData().add(data1);
        return report;
    }

    @Test @DisplayName("RESPONSE_POSITIVE round-trip")
    void positiveResponseRoundTrip() throws Exception {
        CmsReport result = AsduTestUtil.roundTripViaApdu(createSampleReport());
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.RPT1", result.rptID().get());
        assertEquals(42, result.sqNum().get());
        assertEquals(1, result.subSqNum().get());
        assertEquals("IED1.AP1.LD1.LN1.DS1", result.datSet().get());
        assertEquals(1, result.confRev().get());
        assertTrue(result.optFlds().testBit(CmsRcbOptFlds.SEQUENCE_NUMBER));
        assertFalse(result.moreSegmentsFollow().get());
        assertFalse(result.bufOvfl().get());
        assertTrue(result.isFieldPresent("sqNum"));
        assertEquals(1, result.entry().entryData().size());
        assertEquals("IED1.AP1.LD1.LN1.DO1", result.entry().entryData().get(0).reference().get());
        assertTrue(result.entry().entryData().get(0).reason().testBit(CmsReasonCode.DATA_CHANGE));
    }

    @Test @DisplayName("RESPONSE_POSITIVE with only required fields")
    void positiveResponseMinimal() throws Exception {
        CmsReport result = AsduTestUtil.roundTripViaApdu(
                new CmsReport(MessageType.RESPONSE_POSITIVE).rptID("ID").reqId(2));
        assertEquals(2, result.reqId().get());
        assertEquals("ID", result.rptID().get());
        assertFalse(result.isFieldPresent("sqNum"));
        assertTrue(result.entry().entryData().isEmpty());
    }

    @Test @DisplayName("RESPONSE_POSITIVE ASDU round-trip")
    void roundTripAsduOnly() throws Exception {
        CmsReport result = AsduTestUtil.roundTripViaAsdu(createSampleReport());
        assertEquals(1, result.reqId().get());
        assertEquals("IED1.AP1.LD1.LN1.RPT1", result.rptID().get());
    }

    @Test @DisplayName("toString")
    void toStringTest() {
        CmsReport asdu = createSampleReport();
        String str = asdu.toString();
        assertTrue(str.startsWith("(CmsReport) {"));
        assertTrue(str.contains("reqId: (CmsInt16U) 1"));
    }
}
