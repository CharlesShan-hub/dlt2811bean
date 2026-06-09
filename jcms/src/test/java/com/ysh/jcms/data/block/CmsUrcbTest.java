package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsUrcbTest {
    @Test
    public void roundtrip() {
        CmsUrcb a = new CmsUrcb();
        a.rptID.value("urpt01".getBytes());
        a.rptEna.value(false);
        a.datSet.value("dataset1".getBytes());
        a.confRev.value(5L);
        a.optFlds.sequence_number.value(true);
        a.bufTm.value(3000L);
        a.sqNum.value(200);
        a.trgOps.data_change.value(true);
        a.intgPd.value(1000L);
        a.gi.value(false);
        a.resv.value(true);
        byte[] encoded = a.encode();
        CmsUrcb b = new CmsUrcb();
        b.decode(encoded);
        assertArrayEquals("urpt01".getBytes(), b.rptID.value());
        assertFalse(b.rptEna.value());
        assertEquals(5L, b.confRev.value());
        assertTrue(b.resv.value());
    }
}
