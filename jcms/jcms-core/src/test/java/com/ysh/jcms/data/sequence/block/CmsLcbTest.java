package com.ysh.jcms.data.sequence.block;

import com.ysh.jcms.data.sequence.block.CmsLcb;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsLcbTest {
    @Test
    public void roundup() {
        CmsLcb a = new CmsLcb().logEna(true).intgPd(1000L);
        byte[] encoded = a.encode();
        CmsLcb b = new CmsLcb();
        b.decode(encoded);
        assertEquals(a, b);
        System.out.println(a);
    }
}
