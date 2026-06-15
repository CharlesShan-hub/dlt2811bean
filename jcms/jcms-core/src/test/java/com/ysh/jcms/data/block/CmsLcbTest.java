package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsLcbTest {
    @Test
    public void roundtrip() {
        CmsLcb a = new CmsLcb();
        a.logEna.value(true);
        a.intgPd.value(1000L);
        byte[] encoded = a.encode();
        CmsLcb b = new CmsLcb();
        b.decode(encoded);
        assertTrue(b.logEna.value());
        assertEquals(1000L, b.intgPd.value());
    }
}
