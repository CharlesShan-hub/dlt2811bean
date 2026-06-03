package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtcTime")
class CmsUtcTimeTest {

    @Test
    void roundtrip() {
        CmsUtcTime original = CmsUtcTime.fromMillis(1700000000000L);
        byte[] data = original.encode();
        CmsUtcTime decoded = CmsUtcTime.from(data);
        assertEquals(original.seconds_since_epoch, decoded.seconds_since_epoch);
    }

    @Test
    void copy() {
        CmsUtcTime original = CmsUtcTime.fromMillis(1700000000000L);
        CmsUtcTime cloned = original.copy();
        assertEquals(original.seconds_since_epoch, cloned.seconds_since_epoch);
    }
}
