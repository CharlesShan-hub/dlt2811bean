package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGoCbTest {
    @Test
    public void roundtrip() {
        CmsGoCb a = new CmsGoCb();
        a.goEna.value(true);
        a.confRev.value(42L);
        a.ndsCom.value(false);
        byte[] encoded = a.encode();
        CmsGoCb b = new CmsGoCb();
        b.decode(encoded);
        assertTrue(b.goEna.value());
        assertEquals(42L, b.confRev.value());
        assertFalse(b.ndsCom.value());
    }
}
