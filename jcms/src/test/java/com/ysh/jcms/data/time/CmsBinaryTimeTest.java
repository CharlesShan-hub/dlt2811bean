package com.ysh.jcms.data.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBinaryTimeTest {
    @Test
    public void roundtrip() {
        CmsBinaryTime a = new CmsBinaryTime();
        a.msOfDay.value(43200000L);
        a.daysSince1984.value(5000);
        byte[] encoded = a.encode();
        CmsBinaryTime b = new CmsBinaryTime();
        b.decode(encoded);
        assertEquals(43200000L, b.msOfDay.value());
        assertEquals(5000, b.daysSince1984.value());
    }
}
