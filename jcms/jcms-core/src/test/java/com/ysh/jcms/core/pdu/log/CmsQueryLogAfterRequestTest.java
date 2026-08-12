package com.ysh.jcms.core.pdu.log;

import com.ysh.jcms.core.data.sequence.common.CmsBinaryTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQueryLogAfterRequestTest {
    @Test
    public void roundup() {
        CmsQueryLogAfterRequest a = new CmsQueryLogAfterRequest()
            .logReference("logRef")
            .startTime(new CmsBinaryTime().msOfDay(1000L).daysSince1984(5000))
            .entry("00000001".getBytes());
        byte[] encoded = a.encode();

        CmsQueryLogAfterRequest b = new CmsQueryLogAfterRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
