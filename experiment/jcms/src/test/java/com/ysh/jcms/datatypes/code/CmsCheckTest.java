package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsCheck")
class CmsCheckTest {

    @Test
    void roundtrip() {
        CmsCheck original = new CmsCheck(0x02L);
        byte[] data = original.encode();
        CmsCheck decoded = CmsCheck.decode(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void setBit() {
        CmsCheck c = new CmsCheck();
        c.setBit(1, true);
        assertTrue(c.testBit(1));
        assertFalse(c.testBit(0));
    }

    @Test
    void copy() {
        CmsCheck original = new CmsCheck(0x02L);
        CmsCheck cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
