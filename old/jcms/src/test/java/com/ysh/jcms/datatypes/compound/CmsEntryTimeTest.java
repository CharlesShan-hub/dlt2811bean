package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsEntryTime")
class CmsEntryTimeTest {

    @Test
    void roundtrip() {
        CmsEntryTime original = new CmsEntryTime(43200000, 500);
        byte[] data = original.encode();
        CmsEntryTime decoded = CmsEntryTime.from(data);
        assertEquals(original.msOfDay, decoded.msOfDay);
        assertEquals(original.daysSince1984, decoded.daysSince1984);
    }

    @Test
    void zero() {
        CmsEntryTime original = new CmsEntryTime(0, 0);
        byte[] data = original.encode();
        CmsEntryTime decoded = CmsEntryTime.from(data);
        assertEquals(0, decoded.msOfDay);
        assertEquals(0, decoded.daysSince1984);
    }

    @Test
    void maxValues() {
        CmsEntryTime original = new CmsEntryTime(86399999, 65535);
        byte[] data = original.encode();
        CmsEntryTime decoded = CmsEntryTime.from(data);
        assertEquals(original.msOfDay, decoded.msOfDay);
        assertEquals(original.daysSince1984, decoded.daysSince1984);
    }
}
