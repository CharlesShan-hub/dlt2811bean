package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQualityTest {
    @Test
    public void roundtrip() {
        CmsQuality a = new CmsQuality();
        a.validity.value(1);
        a.overflow.value(true);
        a.failure.value(true);
        a.inaccurate.value(true);
        byte[] encoded = a.encode();
        CmsQuality b = new CmsQuality();
        b.decode(encoded);
        assertEquals(1, b.validity.value());
        assertTrue(b.overflow.value());
        assertTrue(b.failure.value());
        assertTrue(b.inaccurate.value());
    }
}
