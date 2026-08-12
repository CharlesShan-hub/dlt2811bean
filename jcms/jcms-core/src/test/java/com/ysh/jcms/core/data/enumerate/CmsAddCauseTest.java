package com.ysh.jcms.core.data.enumerate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAddCauseTest {
    @Test
    public void roundup() {
        CmsAddCause a = new CmsAddCause(CmsAddCause.BLOCKED_BY_INTERLOCKING);
        byte[] encoded = a.encode();
        CmsAddCause b = new CmsAddCause();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
