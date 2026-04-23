package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
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
}