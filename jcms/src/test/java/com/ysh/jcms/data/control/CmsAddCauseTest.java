package com.ysh.jcms.data.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAddCauseTest {
    @Test
    public void roundtrip() {
        CmsAddCause a = new CmsAddCause(CmsAddCause.BLOCKED_BY_INTERLOCKING);
        byte[] encoded = a.encode();
        CmsAddCause b = new CmsAddCause();
        b.decode(encoded);
        assertEquals(CmsAddCause.BLOCKED_BY_INTERLOCKING, b.value());
    }
}
