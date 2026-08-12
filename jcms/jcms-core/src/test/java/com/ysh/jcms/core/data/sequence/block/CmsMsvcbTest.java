package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsMsvcbTest {
    @Test
    public void roundup() {
        CmsMsvcb a = new CmsMsvcb().svEna(true).msvID("msv01").datSet("msdataset").confRev(7L).smpRate(4800);
        byte[] encoded = a.encode();
        CmsMsvcb b = new CmsMsvcb();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
