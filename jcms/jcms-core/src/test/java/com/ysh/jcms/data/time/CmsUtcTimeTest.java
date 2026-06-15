package com.ysh.jcms.data.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsUtcTimeTest {
    @Test
    public void roundtrip() {
        CmsUtcTime a = new CmsUtcTime();
        a.seconds_since_epoch.value(1234567890L);
        a.fraction_of_second.value(500000);
        a.time_quality.leap_seconds_known.value(true);
        byte[] encoded = a.encode();
        CmsUtcTime b = new CmsUtcTime();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
