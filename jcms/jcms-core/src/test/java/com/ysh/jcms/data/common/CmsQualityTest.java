package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsQualityTest {
    @Test
    public void roundup() {
        CmsQuality a = new CmsQuality()
            .validity(1)
            .overflow(true)
            .failure(true)
            .inaccurate(true);
        byte[] encoded = a.encode();
        CmsQuality b = new CmsQuality();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
