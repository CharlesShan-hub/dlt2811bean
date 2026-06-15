package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBrcbTest {
    @Test
    public void roundtrip() {
        CmsBrcb a = new CmsBrcb()
            .rptID("rpt01".getBytes())
            .rptEna(true)
            .datSet("dataset1".getBytes())
            .confRev(3L);
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
        assertEquals(a, b);
    }
}
