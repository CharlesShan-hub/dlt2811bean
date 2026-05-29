package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsCheck")
class CmsCheckTest {

    @Test
    void roundtrip() {
        CmsCheck original = new CmsCheck(0xA055L);
        byte[] data = original.encode();
        CmsCheck decoded = CmsCheck.decode(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void setBit() {
        CmsCheck c = new CmsCheck();
        c.setBit(5, true);
        assertTrue(c.testBit(5));
        assertFalse(c.testBit(4));
    }

    @Test
    void copy() {
        CmsCheck original = new CmsCheck(0xA055L);
        CmsCheck cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
