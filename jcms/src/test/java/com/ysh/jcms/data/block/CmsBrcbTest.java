package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBrcbTest {
    @Test
    public void roundtrip() {
        CmsBrcb a = new CmsBrcb();
        a.rptID.value("rpt01".getBytes());
        a.rptEna.value(true);
        a.datSet.value("dataset1".getBytes());
        a.confRev.value(3L);
        a.optFlds.sequence_number.value(true);
        a.bufTm.value(5000L);
        a.sqNum.value(100);
        a.trgOps.data_change.value(true);
        a.intgPd.value(3000L);
        a.gi.value(false);
        a.purgeBuf.value(true);
        a.entryID.value(new byte[]{1,2,3,4,5,6,7,8});
        byte[] encoded = a.encode();
        System.out.println("encoded " + encoded.length + " bytes");

        CmsBrcb b = new CmsBrcb();
        b.decode(encoded);
        assertArrayEquals("rpt01".getBytes(), b.rptID.value());
        assertTrue(b.rptEna.value());
        assertEquals(3L, b.confRev.value());
        assertTrue(b.optFlds.sequence_number.value());
        assertEquals(5000L, b.bufTm.value());
        assertEquals(100, b.sqNum.value());
        assertTrue(b.trgOps.data_change.value());
        assertEquals(3000L, b.intgPd.value());
        assertFalse(b.gi.value());
        assertTrue(b.purgeBuf.value());
    }
}
