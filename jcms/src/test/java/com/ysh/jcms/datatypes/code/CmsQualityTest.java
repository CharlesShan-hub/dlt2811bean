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
        CmsQuality original = new CmsQuality(0x1FFC); // all flags except VALIDITY bits
        byte[] data = original.encode();
        CmsQuality decoded = CmsQuality.decode(data);
        //System.out.println(decoded);
        assertTrue(decoded.testBit(CmsQuality.FAILURE));
        assertTrue(decoded.testBit(CmsQuality.TEST));
    }

    @Test
    void validityRoundtrip() {
        CmsQuality q = new CmsQuality();
        q.setBit(CmsQuality.QUESTIONABLE, true);
        byte[] enc = q.encode();
        CmsQuality dec = CmsQuality.decode(enc);
        assertTrue(dec.testBit(CmsQuality.QUESTIONABLE));
        assertFalse(dec.testBit(CmsQuality.GOOD));
    }
}
