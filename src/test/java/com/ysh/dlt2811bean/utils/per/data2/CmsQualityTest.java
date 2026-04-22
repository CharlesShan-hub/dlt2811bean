package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsQualityTest {

    @Test
    void encodeDecode_default() throws Exception {
        CmsQuality q = new CmsQuality();
        PerOutputStream pos = new PerOutputStream();
        CmsQuality.encode(pos, q);
        CmsQuality r = CmsQuality.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsQuality.GOOD, r.getValidity());
        assertFalse(r.is(CmsQuality.OVERFLOW));
        assertFalse(r.is(CmsQuality.SOURCE));
    }

    @Test
    void encodeDecode_allFlags() throws Exception {
        CmsQuality q = new CmsQuality();
        q.setValidity(CmsQuality.QUESTIONABLE)
         .set(CmsQuality.OVERFLOW, true)
         .set(CmsQuality.OUT_OF_RANGE, true)
         .set(CmsQuality.BAD_REFERENCE, true)
         .set(CmsQuality.OSCILLATORY, true)
         .set(CmsQuality.FAILURE, true)
         .set(CmsQuality.OLD_DATA, true)
         .set(CmsQuality.INCONSISTENT, true)
         .set(CmsQuality.INACCURATE, true)
         .set(CmsQuality.SOURCE, true)
         .set(CmsQuality.TEST, true)
         .set(CmsQuality.OPERATOR_BLOCKED, true);

        PerOutputStream pos = new PerOutputStream();
        CmsQuality.encode(pos, q);
        CmsQuality r = CmsQuality.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(CmsQuality.QUESTIONABLE, r.getValidity());
        assertTrue(r.is(CmsQuality.OVERFLOW));
        assertTrue(r.is(CmsQuality.OUT_OF_RANGE));
        assertTrue(r.is(CmsQuality.BAD_REFERENCE));
        assertTrue(r.is(CmsQuality.OSCILLATORY));
        assertTrue(r.is(CmsQuality.FAILURE));
        assertTrue(r.is(CmsQuality.OLD_DATA));
        assertTrue(r.is(CmsQuality.INCONSISTENT));
        assertTrue(r.is(CmsQuality.INACCURATE));
        assertTrue(r.is(CmsQuality.SOURCE));
        assertTrue(r.is(CmsQuality.TEST));
        assertTrue(r.is(CmsQuality.OPERATOR_BLOCKED));
    }

    @Test
    void encodeDecode_fromRaw() throws Exception {
        // overflow (bit 2) + test (bit 11) = (1<<2)|(1<<11) = 4|2048 = 0x0804
        CmsQuality q = new CmsQuality(0x0804);
        assertTrue(q.is(CmsQuality.OVERFLOW));
        assertTrue(q.is(CmsQuality.TEST));
        assertFalse(q.is(CmsQuality.FAILURE));
        assertEquals(CmsQuality.GOOD, q.getValidity());
        assertEquals(0x0804, q.toRaw());

        PerOutputStream pos = new PerOutputStream();
        CmsQuality.encode(pos, q);
        CmsQuality r = CmsQuality.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0x0804, r.toRaw());
    }

    @Test
    void encodeDecode_specificScenario() throws Exception {
        CmsQuality q = new CmsQuality();
        q.setValidity(CmsQuality.INVALID).set(CmsQuality.SOURCE, true);

        PerOutputStream pos = new PerOutputStream();
        CmsQuality.encode(pos, q);
        CmsQuality r = CmsQuality.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(CmsQuality.INVALID, r.getValidity());
        assertTrue(r.is(CmsQuality.SOURCE));
        assertFalse(r.is(CmsQuality.OVERFLOW));
        assertFalse(r.is(CmsQuality.FAILURE));
    }
}
