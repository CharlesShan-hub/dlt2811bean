package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGoCbTest {
    @Test
    public void roundup() {
        CmsGoCb a = new CmsGoCb()
            .goEna(true)
            .confRev(42L)
            .ndsCom(false);
        byte[] encoded = a.encode();
        CmsGoCb b = new CmsGoCb();
        b.decode(encoded);
        System.out.println(a);
        System.out.println(b);
        assertEquals(a, b);
    }
}
