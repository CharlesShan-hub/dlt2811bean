package com.ysh.jcms.datatypes.code;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQuality")
class CmsQualityTest {

    @Test
    void constructAndTestBit() {
        CmsQuality q = new CmsQuality();
        assertEquals(0, q.get());

        q.setBit(2, true);
        q.setBit(11, true);
        assertTrue(q.testBit(2));
        assertTrue(q.testBit(11));
        assertFalse(q.testBit(3));
    }

    @Test
    void roundtrip() {
        CmsQuality original = new CmsQuality(0x1FFF);
        byte[] data = original.encode();
        CmsQuality decoded = CmsQuality.decode(data);
        assertTrue(decoded.testBit(0) == original.testBit(0));
    }

    @Test
    void getBits() {
        CmsQuality q = new CmsQuality();
        q.setBits(0, 2, 3);
        assertEquals(3, q.getBits(0, 2));
    }
}
