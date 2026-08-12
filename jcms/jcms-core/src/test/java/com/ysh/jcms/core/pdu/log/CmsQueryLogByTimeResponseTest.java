package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.bitarray.CmsReasonCode;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.core.data.sequence.log.CmsLogDataEntry;
import com.ysh.jcms.core.data.sequence.log.CmsLogEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQueryLogByTimeResponseTest {
    @Test
    public void roundup() {
        CmsQueryLogByTimeResponse a = new CmsQueryLogByTimeResponse()
            .logEntry(Arrays.asList(
                new CmsLogEntry()
                    .timeOfEntry(new CmsBinaryTime().msOfDay(60000L).daysSince1984(5001))
                    .entryID("00000001".getBytes())
                    .entryData(Arrays.asList(
                        new CmsLogDataEntry()
                            .reference("ref1_0")
                            .fc(CmsFC.ST)
                            .value(new CmsData().alt_int32(10))
                            .reason(new CmsReasonCode().data_change(true).quality_change(true)),
                        new CmsLogDataEntry()
                            .reference("ref1_1")
                            .fc(CmsFC.MX)
                            .value(new CmsData().alt_boolean(true))
                            .reason(new CmsReasonCode().data_update(true)))),
                new CmsLogEntry()
                    .timeOfEntry(new CmsBinaryTime().msOfDay(120000L).daysSince1984(5002))
                    .entryID("00000002".getBytes())
                    .entryData(Arrays.asList(
                        new CmsLogDataEntry()
                            .reference("ref2_0")
                            .fc(CmsFC.ST)
                            .value(new CmsData().alt_int32(20))
                            .reason(new CmsReasonCode().data_change(true))))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsQueryLogByTimeResponse b = new CmsQueryLogByTimeResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
