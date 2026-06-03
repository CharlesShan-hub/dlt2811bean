package com.ysh.jcms.datatypes.compound;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeStamp")
class CmsTimeStampTest {

    @Test
    void roundtrip() {
        CmsTimeStamp original = new CmsTimeStamp(1700000000L, 456L);
        byte[] data = original.encode();
        CmsTimeStamp decoded = CmsTimeStamp.decode(data);
        assertEquals(original.secondsSinceEpoch(), decoded.secondsSinceEpoch());
        assertEquals(original.fractional(), decoded.fractional());
    }

    @Test
    void copy() {
        CmsTimeStamp original = new CmsTimeStamp(1700000000L, 456L);
        CmsTimeStamp cloned = original.copy();
        assertEquals(original.secondsSinceEpoch(), cloned.secondsSinceEpoch());
        assertEquals(original.fractional(), cloned.fractional());
    }
}
