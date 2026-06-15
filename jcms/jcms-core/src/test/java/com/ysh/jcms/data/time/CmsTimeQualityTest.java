package com.ysh.jcms.data.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTimeQualityTest {
    @Test
    public void roundup() {
        CmsTimeQuality a = new CmsTimeQuality()
            .leap_seconds_known(true)
            .clock_failure(true)
            .clock_not_synchronized(false)
            .precision(15);
        byte[] encoded = a.encode();
        CmsTimeQuality b = new CmsTimeQuality();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
