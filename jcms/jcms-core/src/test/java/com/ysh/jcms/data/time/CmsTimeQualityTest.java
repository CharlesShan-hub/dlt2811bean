package com.ysh.jcms.data.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsTimeQualityTest {
    @Test
    public void roundtrip() {
        CmsTimeQuality a = new CmsTimeQuality();
        a.leap_seconds_known.value(true);
        a.clock_failure.value(true);
        a.clock_not_synchronized.value(false);
        a.precision.value(15);
        byte[] encoded = a.encode();
        CmsTimeQuality b = new CmsTimeQuality();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
