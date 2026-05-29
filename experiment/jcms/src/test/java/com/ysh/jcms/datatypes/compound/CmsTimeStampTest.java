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
        assertEquals(original.getSecondsSinceEpoch(), decoded.getSecondsSinceEpoch());
        assertEquals(original.getFractional(), decoded.getFractional());
    }

    @Test
    void copy() {
        CmsTimeStamp original = new CmsTimeStamp(1700000000L, 456L);
        CmsTimeStamp cloned = original.copy();
        assertEquals(original.getSecondsSinceEpoch(), cloned.getSecondsSinceEpoch());
        assertEquals(original.getFractional(), cloned.getFractional());
    }
}
