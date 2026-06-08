package com.ysh.jcms.datatype.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsEntryTime")
class CmsEntryTimeTest {

    private CmsEntryTime get() { return (CmsEntryTime)(new CmsEntryTime().test()); }

    @Test
    void roundtrip() {
        CmsEntryTime original = get().set(2024, 6, 6, 10, 30, 45, 500);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void setFromEpochMillis() {
        CmsEntryTime original = get().set(1718015445500L);
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void nowRoundtrip() {
        CmsEntryTime original = get().now();
        assertEquals(original, get().decode(original.encode()));
    }

    @Test
    void subTypeAccess() {
        CmsEntryTime t = get().set(1718015445500L);
        assertTrue(t.msOfDay().value() > 0);
    }
}
