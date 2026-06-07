package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsEntryTime")
class CmsEntryTimeTest {

    @Test
    void roundtrip() {
        CmsEntryTime original = new CmsEntryTime().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, new CmsEntryTime().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        CmsEntryTime original = new CmsEntryTime().set(1718015445500L);
        assertEquals(original, new CmsEntryTime().decode(original.encode()));
    }

    @Test
    void nowRoundtrip() {
        CmsEntryTime original = new CmsEntryTime().now();
        assertEquals(original, new CmsEntryTime().decode(original.encode()));
    }

    @Test
    void subTypeAccess() {
        CmsEntryTime t = new CmsEntryTime().set(1718015445500L);
        assertTrue(t.msOfDay().value() > 0);
    }
}
