package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbTest {
    @Test
    public void roundtrip() {
        CmsMsvcb a = new CmsMsvcb();
        a.svEna.value(true);
        a.msvID.value("msv01".getBytes());
        a.datSet.value("msdataset".getBytes());
        a.confRev.value(7L);
        a.smpRate.value(4800);
        byte[] encoded = a.encode();
        CmsMsvcb b = new CmsMsvcb();
        b.decode(encoded);
        assertTrue(b.svEna.value());
        assertEquals(7L, b.confRev.value());
        assertEquals(4800, b.smpRate.value());
    }
}
