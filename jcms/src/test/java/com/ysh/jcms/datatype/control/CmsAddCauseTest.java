package com.ysh.jcms.datatype.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAddCause")
class CmsAddCauseTest {

    private CmsAddCause get() { return (CmsAddCause)(new CmsAddCause().test()); }

    @Test
    void unknown() {
        CmsAddCause a = get().value(CmsAddCause.UNKNOWN);
        CmsAddCause b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void blockedBySyncheck() {
        CmsAddCause a = get().value(CmsAddCause.BLOCKED_BY_SYNCHECK);
        CmsAddCause b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void none() {
        CmsAddCause a = get().value(CmsAddCause.NONE);
        CmsAddCause b = get().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, get().value());
    }

    @Test
    void decodeOverwrites() {
        CmsAddCause src = get().value(CmsAddCause.NONE);
        CmsAddCause target = get().decode(src.encode());
        assertEquals(src, target);
    }

    @Test
    void constantsUnique() {
        assertNotEquals(CmsAddCause.UNKNOWN, CmsAddCause.NONE);
        assertTrue(CmsAddCause.INVALID_POSITION < CmsAddCause.INCONSISTENT_PARAMETERS);
    }
}
