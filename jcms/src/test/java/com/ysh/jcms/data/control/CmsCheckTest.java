package com.ysh.jcms.data.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCheckTest {
    @Test
    public void roundtrip() {
        CmsCheck a = new CmsCheck();
        a.syncheck.value(true);
        a.interlock_check.value(false);
        byte[] encoded = a.encode();
        CmsCheck b = new CmsCheck();
        b.decode(encoded);
        assertTrue(b.syncheck.value());
        assertFalse(b.interlock_check.value());
    }
}
