package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbTest {
    @Test
    public void roundtrip() {
        CmsMsvcb a = new CmsMsvcb()
            .svEna(true)
            .msvID("msv01".getBytes())
            .datSet("msdataset".getBytes())
            .confRev(7L)
            .smpRate(4800);
        byte[] encoded = a.encode();
        CmsMsvcb b = new CmsMsvcb();
        b.decode(encoded);
        assertTrue(b.svEna.value());
        assertEquals(7L, b.confRev.value());
        assertEquals(4800, b.smpRate.value());
    }
}
