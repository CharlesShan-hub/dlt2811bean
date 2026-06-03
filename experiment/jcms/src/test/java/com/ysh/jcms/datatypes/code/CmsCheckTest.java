package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsCheck")
class CmsCheckTest {

    @Test
    void roundup() {
        CmsCheck original = new CmsCheck();
        original.setBit(CmsCheck.INTERLOCK_CHECK, true);
        byte[] data = original.encode();
        CmsCheck decoded = CmsCheck.decode(data);
        //System.out.println(decoded); // (CmsCheck) 2
        //System.out.println(decoded.testBit(CmsCheck.INTERLOCK_CHECK)); // true
        //System.out.println(decoded.testBit(CmsCheck.SYNCHROCHECK)); // false
        assertEquals(decoded.testBit(0), original.testBit(0));
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
        CmsCheck original = new CmsCheck(0x02);
        CmsCheck cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
