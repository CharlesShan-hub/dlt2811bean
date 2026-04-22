package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.utils.per.data2.CmsTimeQuality.*;
import static org.junit.jupiter.api.Assertions.*;

class CmsTimeQualityTest {

    @Test
    void default_isAllZero() {
        CmsTimeQuality tq = new CmsTimeQuality();
        assertFalse(tq.is(LEAP_SECOND_KNOWN));
        assertFalse(tq.is(CLOCK_FAULT));
        assertFalse(tq.is(CLOCK_NOT_SYNCED));
        assertEquals(0, tq.getSubSecondPrecision());
    }

    @Test
    void set_singleFlag() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.set(CLOCK_FAULT, true);
        assertTrue(tq.is(CLOCK_FAULT));
        assertFalse(tq.is(LEAP_SECOND_KNOWN));
        assertFalse(tq.is(CLOCK_NOT_SYNCED));
    }

    @Test
    void set_allFlags() {
        CmsTimeQuality tq = new CmsTimeQuality()
            .set(LEAP_SECOND_KNOWN, true)
            .set(CLOCK_FAULT, true)
            .set(CLOCK_NOT_SYNCED, true);
        assertTrue(tq.is(LEAP_SECOND_KNOWN));
        assertTrue(tq.is(CLOCK_FAULT));
        assertTrue(tq.is(CLOCK_NOT_SYNCED));
    }

    @Test
    void set_thenClear() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.set(CLOCK_FAULT, true);
        assertTrue(tq.is(CLOCK_FAULT));
        tq.set(CLOCK_FAULT, false);
        assertFalse(tq.is(CLOCK_FAULT));
    }

    @Test
    void subSecondPrecision_setAndGet() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setSubSecondPrecision(10);
        assertEquals(10, tq.getSubSecondPrecision());
    }

    @Test
    void subSecondPrecision_maxValue() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setSubSecondPrecision(24); // max valid
        assertEquals(24, tq.getSubSecondPrecision());
    }

    @Test
    void subSecondPrecision_doesNotAffectFlags() {
        CmsTimeQuality tq = new CmsTimeQuality()
            .set(LEAP_SECOND_KNOWN, true)
            .setSubSecondPrecision(5);
        assertTrue(tq.is(LEAP_SECOND_KNOWN));
        assertFalse(tq.is(CLOCK_FAULT));
        assertEquals(5, tq.getSubSecondPrecision());
    }

    @Test
    void construct_fromRaw() {
        // bit0=leapSecondKnown, bit1=clockFault, bits3~7=precision=24(0b11000)
        // raw = 0b11000_011 = 0xC3
        CmsTimeQuality tq = new CmsTimeQuality(0xC3);
        assertTrue(tq.is(LEAP_SECOND_KNOWN));
        assertTrue(tq.is(CLOCK_FAULT));
        assertFalse(tq.is(CLOCK_NOT_SYNCED));
        assertEquals(24, tq.getSubSecondPrecision());
    }

    @Test
    void encodeDecode_roundtrip() throws Exception {
        CmsTimeQuality tq = new CmsTimeQuality()
            .set(CLOCK_NOT_SYNCED, true)
            .setSubSecondPrecision(3);

        PerOutputStream pos = new PerOutputStream();
        CmsTimeQuality.encode(pos, tq);

        CmsTimeQuality result = CmsTimeQuality.decode(new PerInputStream(pos.toByteArray()));

        assertFalse(result.is(LEAP_SECOND_KNOWN));
        assertFalse(result.is(CLOCK_FAULT));
        assertTrue(result.is(CLOCK_NOT_SYNCED));
        assertEquals(3, result.getSubSecondPrecision());
    }

    @Test
    void is_outOfRange_throws() {
        CmsTimeQuality tq = new CmsTimeQuality();
        assertThrows(IllegalArgumentException.class, () -> tq.is(-1));
        assertThrows(IllegalArgumentException.class, () -> tq.is(8));
    }

    @Test
    void toRaw_matchesRawConstructor() {
        CmsTimeQuality tq = new CmsTimeQuality()
            .set(LEAP_SECOND_KNOWN, true)
            .setSubSecondPrecision(10);
        int raw = tq.toRaw();
        CmsTimeQuality tq2 = new CmsTimeQuality(raw);
        assertEquals(raw, tq2.toRaw());
        assertTrue(tq2.is(LEAP_SECOND_KNOWN));
        assertEquals(10, tq2.getSubSecondPrecision());
    }
}
