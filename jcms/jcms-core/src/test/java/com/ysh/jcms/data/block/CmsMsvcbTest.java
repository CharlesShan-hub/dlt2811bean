package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbTest {
    @Test
    public void roundup() {
        CmsMsvcb a = new CmsMsvcb().svEna(true).msvID("msv01".getBytes()).datSet("msdataset".getBytes()).confRev(7L).smpRate(4800);
        byte[] encoded = a.encode();
        CmsMsvcb b = new CmsMsvcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
