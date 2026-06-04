package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsLcbOptFlds")
class CmsLcbOptFldsTest {

    @Test
    void roundtrip() {
        CmsLcbOptFlds original = new CmsLcbOptFlds(0x01);
        byte[] data = original.encode();
        CmsLcbOptFlds decoded = CmsLcbOptFlds.from(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void copy() {
        CmsLcbOptFlds original = new CmsLcbOptFlds(0x01);
        CmsLcbOptFlds cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
