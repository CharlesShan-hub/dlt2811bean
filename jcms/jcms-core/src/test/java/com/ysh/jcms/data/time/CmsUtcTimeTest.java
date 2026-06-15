package com.ysh.jcms.data.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsUtcTimeTest {
    @Test
    public void roundup() {
        CmsUtcTime a = new CmsUtcTime()
            .seconds_since_epoch(1234567890L)
            .fraction_of_second(500000)
            .time_quality(new CmsTimeQuality().leap_seconds_known(true));
        byte[] encoded = a.encode();
        CmsUtcTime b = new CmsUtcTime();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
