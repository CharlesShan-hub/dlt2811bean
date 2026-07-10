package com.ysh.jcms.svc.report;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.data.common.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReportTest {

    @Test
    public void get_brcb_request() {
        CmsGetBrcbValuesRequest a = new CmsGetBrcbValuesRequest();
        a.reqId.value(1);
        a.reference.add(new CmsObjectReference("brcbRef".getBytes()));
        byte[] encoded = a.encode();

        CmsGetBrcbValuesRequest b = new CmsGetBrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_brcb_response_with_array() {
        CmsGetBrcbValuesResponse a = new CmsGetBrcbValuesResponse();
        a.reqId.value(10);
        CmsRcbValueChoice v1 = new CmsRcbValueChoice();
        v1.choice.value(CmsRcbValueChoice.VALUE);
        v1.altValue.rptID.value("brcbRpt".getBytes());
        v1.altValue.rptEna.value(true);
        v1.altValue.confRev.value(100L);
        v1.altValue.sqNum.value(5);
        v1.altValue.gi.value(false);
        v1.altValue.purgeBuf.value(true);
        v1.altValue.entryID.value(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        a.brcb.add(v1);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetBrcbValuesResponse b = new CmsGetBrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_brcb_error() {
        CmsGetBrcbValuesError a = new CmsGetBrcbValuesError();
        a.reqId.value(99);
        a.serviceError.value(CmsServiceError.INSTANCE_IN_USE);
        byte[] encoded = a.encode();

        CmsGetBrcbValuesError b = new CmsGetBrcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_urcb_request() {
        CmsGetUrcbValuesRequest a = new CmsGetUrcbValuesRequest();
        a.reqId.value(20);
        a.reference.add(new CmsObjectReference("urcbRef".getBytes()));
        byte[] encoded = a.encode();

        CmsGetUrcbValuesRequest b = new CmsGetUrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_urcb_response_with_array() {
        CmsGetUrcbValuesResponse a = new CmsGetUrcbValuesResponse();
        a.reqId.value(25);
        CmsRcbValueChoice v1 = new CmsRcbValueChoice();
        v1.choice.value(CmsRcbValueChoice.VALUE);
        v1.altValue.rptID.value("urcbRpt".getBytes());
        v1.altValue.rptEna.value(true);
        v1.altValue.confRev.value(50L);
        v1.altValue.entryID.value(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        a.urcb.add(v1);
        a.moreFollows.value(false);
        byte[] encoded = a.encode();

        CmsGetUrcbValuesResponse b = new CmsGetUrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void get_urcb_error() {
        CmsGetUrcbValuesError a = new CmsGetUrcbValuesError();
        a.reqId.value(88);
        a.serviceError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
        byte[] encoded = a.encode();

        CmsGetUrcbValuesError b = new CmsGetUrcbValuesError();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_brcb_request() {
        CmsSetBrcbValuesRequest a = new CmsSetBrcbValuesRequest();
        CmsSetBrcbEntry e1 = new CmsSetBrcbEntry();
        e1.reference.value("setBrcb1".getBytes());
        e1.rptIdPresent.value(true);
        e1.rptId.value("newRpt".getBytes());
        e1.rptEnaPresent.value(true);
        e1.rptEna.value(true);
        a.brcb.add(e1);
        a.reqId.value(30);
        byte[] encoded = a.encode();

        CmsSetBrcbValuesRequest b = new CmsSetBrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_brcb_response() {
        CmsSetBrcbValuesResponse a = new CmsSetBrcbValuesResponse();
        a.reqId.value(31);
        byte[] encoded = a.encode();

        CmsSetBrcbValuesResponse b = new CmsSetBrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_urcb_request() {
        CmsSetUrcbValuesRequest a = new CmsSetUrcbValuesRequest();
        CmsSetUrcbEntry e1 = new CmsSetUrcbEntry();
        e1.reference.value("setUrcb1".getBytes());
        e1.rptIdPresent.value(true);
        e1.rptId.value("newUrcbRpt".getBytes());
        e1.rptEnaPresent.value(true);
        e1.rptEna.value(true);
        a.urcb.add(e1);
        a.reqId.value(40);
        byte[] encoded = a.encode();

        CmsSetUrcbValuesRequest b = new CmsSetUrcbValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_urcb_response() {
        CmsSetUrcbValuesResponse a = new CmsSetUrcbValuesResponse();
        a.reqId.value(41);
        byte[] encoded = a.encode();

        CmsSetUrcbValuesResponse b = new CmsSetUrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void report_pdu_minimal() {
        CmsReport a = new CmsReport();
        a.reqId.value(50);
        a.rptID.value("rpt01".getBytes());
        a.optFlds.sequence_number.value(true);
        a.sqNumPresent.value(false);
        a.subSeqNumPresent.value(false);
        a.moreSegmentsFollowPresent.value(false);
        a.dataSetPresent.value(false);
        a.bufOvflPresent.value(false);
        a.confRevPresent.value(false);
        a.entry.timeOfEntryPresent.value(false);
        a.entry.entryIdPresent.value(false);
        byte[] encoded = a.encode();

        CmsReport b = new CmsReport();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void report_pdu_with_entry_data() {
        CmsReport a = new CmsReport();
        a.reqId.value(60);
        a.rptID.value("rpt02".getBytes());
        a.optFlds.sequence_number.value(true);
        a.sqNumPresent.value(false);
        a.subSeqNumPresent.value(false);
        a.moreSegmentsFollowPresent.value(false);
        a.dataSetPresent.value(false);
        a.bufOvflPresent.value(false);
        a.confRevPresent.value(false);
        a.entry.timeOfEntryPresent.value(true);
        a.entry.timeOfEntry.msOfDay.value(3600000L);
        a.entry.timeOfEntry.daysSince1984.value(15000);
        a.entry.entryIdPresent.value(false);
        CmsReportDataEntry ed1 = new CmsReportDataEntry();
        ed1.refPresent.value(false);
        ed1.fcPresent.value(false);
        ed1.id.value(1);
        ed1.value.choice.value(CmsData.CHOICE_BOOLEAN);
        ed1.value.alt_boolean.value(true);
        ed1.reasonPresent.value(false);
        a.entry.entryData.add(ed1);
        byte[] encoded = a.encode();

        CmsReport b = new CmsReport();
        b.entry.entryData.add(new CmsReportDataEntry());
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void set_brcb_result() {
        CmsSetBrcbResult a = new CmsSetBrcbResult();
        a.errorPresent.value(true);
        a.error.value(CmsServiceError.NO_ERROR);
        a.rptIdErrPresent.value(false);
        a.rptEnaErrPresent.value(false);
        a.datSetErrPresent.value(false);
        a.optFldsErrPresent.value(false);
        a.bufTmErrPresent.value(false);
        a.trgOpsErrPresent.value(false);
        a.intgPdErrPresent.value(false);
        a.giErrPresent.value(false);
        a.purgeBufErrPresent.value(false);
        a.entryIdErrPresent.value(false);
        a.resvTmsErrPresent.value(false);
        /* encode/decode as part of SetBrcbValuesError */
        CmsSetBrcbValuesError pdu = new CmsSetBrcbValuesError();
        pdu.reqId.value(80);
        pdu.result.add(a);
        byte[] encoded = pdu.encode();

        CmsSetBrcbValuesError b = new CmsSetBrcbValuesError();
        b.decode(encoded);
        assertEquals(80, b.reqId.value());
        assertEquals(1, b.result.size());
        assertTrue(b.result.get(0).errorPresent.value());
        assertEquals(CmsServiceError.NO_ERROR, b.result.get(0).error.value());
    }

    @Test
    public void set_urcb_result() {
        CmsSetUrcbResult a = new CmsSetUrcbResult();
        a.errorPresent.value(true);
        a.error.value(CmsServiceError.INSTANCE_IN_USE);
        a.rptIdErrPresent.value(false);
        a.rptEnaErrPresent.value(false);
        a.datSetErrPresent.value(false);
        a.optFldsErrPresent.value(false);
        a.bufTmErrPresent.value(false);
        a.trgOpsErrPresent.value(false);
        a.intgPdErrPresent.value(false);
        a.giErrPresent.value(false);
        a.resvErrPresent.value(false);
        CmsSetUrcbValuesError pdu = new CmsSetUrcbValuesError();
        pdu.reqId.value(90);
        pdu.result.add(a);
        byte[] encoded = pdu.encode();

        CmsSetUrcbValuesError b = new CmsSetUrcbValuesError();
        b.decode(encoded);
        assertEquals(b, pdu);
    }
}
