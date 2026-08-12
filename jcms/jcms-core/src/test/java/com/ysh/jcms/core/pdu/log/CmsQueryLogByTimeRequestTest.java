package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQueryLogByTimeRequestTest {
    @Test
    public void roundup() {
        CmsQueryLogByTimeRequest a = new CmsQueryLogByTimeRequest()
            .logReference("logRef")
            .startTime(new CmsBinaryTime().msOfDay(1000L).daysSince1984(5000))
            .stopTime(new CmsBinaryTime().msOfDay(2000L).daysSince1984(5000))
            .entryAfter("00000002".getBytes());
        byte[] encoded = a.encode();

        CmsQueryLogByTimeRequest b = new CmsQueryLogByTimeRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
