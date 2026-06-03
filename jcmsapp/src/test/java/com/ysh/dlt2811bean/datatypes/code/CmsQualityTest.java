package com.ysh.dlt2811bean.datatypes.code;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsQuality")
class CmsQualityTest {

    @Test
    @DisplayName("construct, setBit, testBit, encode/decode")
    void testAll() throws Exception {
        CmsQuality q = new CmsQuality();
        assertEquals(0L, q.get());

        q.setBit(CmsQuality.OVERFLOW, true);
        q.setBit(CmsQuality.TEST, true);
        assertTrue(q.testBit(CmsQuality.OVERFLOW));
        assertTrue(q.testBit(CmsQuality.TEST));
        assertFalse(q.testBit(CmsQuality.FAILURE));

        PerOutputStream pos = new PerOutputStream();
        q.encode(pos);

        CmsQuality decoded = new CmsQuality().decode(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.testBit(CmsQuality.OVERFLOW));
        assertTrue(decoded.testBit(CmsQuality.TEST));
        assertFalse(decoded.testBit(CmsQuality.FAILURE));
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsQuality.write(pos, 0x14L); // bits 2,4 (OVERFLOW, BAD_REFERENCE)

        CmsQuality decoded = CmsQuality.read(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.testBit(CmsQuality.OVERFLOW));
        assertTrue(decoded.testBit(CmsQuality.BAD_REFERENCE));
        assertFalse(decoded.testBit(CmsQuality.FAILURE));
    }
}