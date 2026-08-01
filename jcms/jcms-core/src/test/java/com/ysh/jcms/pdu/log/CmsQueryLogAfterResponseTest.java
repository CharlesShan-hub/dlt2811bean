package com.ysh.jcms.pdu.log;

import com.ysh.jcms.data.bitarray.CmsReasonCode;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.scalar.CmsFC;
import com.ysh.jcms.data.sequence.common.CmsBinaryTime;
import com.ysh.jcms.data.sequence.log.CmsLogDataEntry;
import com.ysh.jcms.data.sequence.log.CmsLogEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQueryLogAfterResponseTest {
    @Test
    public void roundup() {
        CmsQueryLogAfterResponse a = new CmsQueryLogAfterResponse()
            .logEntry(Arrays.asList(
                new CmsLogEntry()
                    .timeOfEntry(new CmsBinaryTime().msOfDay(60000L).daysSince1984(5001))
                    .entryID("00000001".getBytes())
                    .entryData(Arrays.asList(
                        new CmsLogDataEntry()
                            .reference("ref1_0")
                            .fc(CmsFC.ST)
                            .value(new CmsData().alt_int32(10))
                            .reason(new CmsReasonCode().data_change(true))))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsQueryLogAfterResponse b = new CmsQueryLogAfterResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
