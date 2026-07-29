package com.ysh.jcms.data.sequence.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBinaryTimeTest {
    @Test
    public void roundup() {
        CmsBinaryTime a = new CmsBinaryTime().msOfDay(43200000L).daysSince1984(5000);
        byte[] encoded = a.encode();
        CmsBinaryTime b = new CmsBinaryTime();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
