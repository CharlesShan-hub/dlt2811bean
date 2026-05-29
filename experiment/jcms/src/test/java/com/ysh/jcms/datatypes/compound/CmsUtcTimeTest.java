package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtcTime")
class CmsUtcTimeTest {

    @Test
    void roundtrip() {
        CmsUtcTime original = new CmsUtcTime(1700000000000L);
        byte[] data = original.encode();
        CmsUtcTime decoded = CmsUtcTime.decode(data);
        assertEquals(original.getSecondsSinceEpoch(), decoded.getSecondsSinceEpoch());
    }

    @Test
    void copy() {
        CmsUtcTime original = new CmsUtcTime(1700000000000L);
        CmsUtcTime cloned = original.copy();
        assertEquals(original.getSecondsSinceEpoch(), cloned.getSecondsSinceEpoch());
    }
}
