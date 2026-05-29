package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeQuality")
class CmsTimeQualityTest {

    @Test
    void roundtrip() {
        CmsTimeQuality original = new CmsTimeQuality(0x05L);
        byte[] data = original.encode();
        CmsTimeQuality decoded = CmsTimeQuality.decode(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void copy() {
        CmsTimeQuality original = new CmsTimeQuality(0x05L);
        CmsTimeQuality cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
