package com.ysh.jcms.core.pdu.report;

import com.ysh.jcms.core.data.bitarray.CmsRcbOptFlds;
import com.ysh.jcms.core.data.bitarray.CmsReasonCode;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.report.CmsReportDataEntry;
import com.ysh.jcms.core.data.sequence.report.CmsReportEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReportTest {
    @Test
    public void minimal() {
        CmsReport a = new CmsReport()
            .rptID("rpt01")
            .optFlds(new CmsRcbOptFlds().sequence_number(true));
        byte[] encoded = a.encode();

        CmsReport b = new CmsReport();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void withEntryData() {
        CmsReport a = new CmsReport()
            .rptID("rpt02")
            .optFlds(new CmsRcbOptFlds().sequence_number(true).report_time_stamp(true))
            .sqNum(5)
            .confRev(100L)
            .entry(new CmsReportEntry()
                .timeOfEntry(new CmsBinaryTime().msOfDay(3600000L).daysSince1984(15000))
                .entryID("00000001".getBytes())
                .entryData(Arrays.asList(
                    new CmsReportDataEntry()
                        .reference("ref1")
                        .fc(CmsFC.ST)
                        .id(1)
                        .value(new CmsData().alt_boolean(true))
                        .reason(new CmsReasonCode().data_change(true)))));
        byte[] encoded = a.encode();

        CmsReport b = new CmsReport();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
