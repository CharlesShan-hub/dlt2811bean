package com.ysh.jcms.core.data.sequence.block;

import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGoCbTest {
    @Test
    public void roundup() {
        CmsGoCb a = new CmsGoCb().goEna(true).confRev(42L).ndsCom(false);
        byte[] encoded = a.encode();
        CmsGoCb b = new CmsGoCb();
        b.decode(encoded);
        System.out.println(a);
        System.out.println(b);
        assertEquals(a, b);
    }

    @Test
    public void roundup2() {
        CmsGoCb a = new CmsGoCb()
                // .goID("Test000")
                .datSet("Test111").goEna(true).confRev(42L).ndsCom(false);
        byte[] encoded = a.encode();
        CmsGoCb b = new CmsGoCb();
        b.decode(encoded);
        System.out.println(a);
        System.out.println(b);
        assertEquals(a, b);
    }
}
