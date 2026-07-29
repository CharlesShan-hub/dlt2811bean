package com.ysh.jcms.data.sequence.time;

import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.data.sequence.time.CmsUtcTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsUtcTimeTest {
    @Test
    public void roundup() {
        CmsUtcTime a = new CmsUtcTime().secondsSinceEpoch(1234567890L).fractionOfSecond(500000)
                .timeQuality(new CmsTimeQuality().leap_seconds_known(true));
        byte[] encoded = a.encode();
        CmsUtcTime b = new CmsUtcTime();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
