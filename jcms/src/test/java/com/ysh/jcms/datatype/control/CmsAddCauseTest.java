package com.ysh.jcms.datatype.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsAddCause")
class CmsAddCauseTest {

    @Test
    void unknown() {
        CmsAddCause original = new CmsAddCause().value(CmsAddCause.UNKNOWN);
        assertEquals(original, new CmsAddCause().decode(original.encode()));
    }

    @Test
    void blockedBySyncheck() {
        CmsAddCause original = new CmsAddCause().value(CmsAddCause.BLOCKED_BY_SYNCHECK);
        assertEquals(original, new CmsAddCause().decode(original.encode()));
    }

    @Test
    void none() {
        CmsAddCause original = new CmsAddCause().value(CmsAddCause.NONE);
        assertEquals(original, new CmsAddCause().decode(original.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsAddCause().value());
    }

    @Test
    void decodeOverwrites() {
        CmsAddCause target = new CmsAddCause().value(CmsAddCause.SELECT_FAILED);
        target.decode(new CmsAddCause().value(CmsAddCause.NONE).encode());
        assertEquals(new CmsAddCause().value(CmsAddCause.NONE), target);
    }

    @Test
    void constantsUnique() {
        assertNotEquals(CmsAddCause.UNKNOWN, CmsAddCause.NONE);
        assertTrue(CmsAddCause.INVALID_POSITION < CmsAddCause.INCONSISTENT_PARAMETERS);
    }
}
