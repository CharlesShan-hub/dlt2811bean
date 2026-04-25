package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.code.CmsTimeQuality;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.data.code.CmsTimeQuality.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsTimeQuality")
class CmsTimeQualityTest {

    @Test
    @DisplayName("default is all zero")
    void default_isAllZero() {
        CmsTimeQuality tq = new CmsTimeQuality();
        assertFalse(tq.testBit(LEAP_SECOND_KNOWN));
        assertFalse(tq.testBit(CLOCK_FAULT));
        assertFalse(tq.testBit(CLOCK_NOT_SYNCED));
        assertEquals(0, tq.getSubSecondPrecision());
        assertEquals(0L, tq.get());
    }

    @Test
    @DisplayName("set single flag")
    void set_singleFlag() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setBit(CLOCK_FAULT, true);
        assertTrue(tq.testBit(CLOCK_FAULT));
        assertFalse(tq.testBit(LEAP_SECOND_KNOWN));
        assertFalse(tq.testBit(CLOCK_NOT_SYNCED));
    }

    @Test
    @DisplayName("set all flags")
    void set_allFlags() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setBit(LEAP_SECOND_KNOWN, true);
        tq.setBit(CLOCK_FAULT, true);
        tq.setBit(CLOCK_NOT_SYNCED, true);
        assertTrue(tq.testBit(LEAP_SECOND_KNOWN));
        assertTrue(tq.testBit(CLOCK_FAULT));
        assertTrue(tq.testBit(CLOCK_NOT_SYNCED));
    }

    @Test
    @DisplayName("set then clear")
    void set_thenClear() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setBit(CLOCK_FAULT, true);
        assertTrue(tq.testBit(CLOCK_FAULT));
        tq.setBit(CLOCK_FAULT, false);
        assertFalse(tq.testBit(CLOCK_FAULT));
    }

    @Test
    @DisplayName("sub-second precision set and get")
    void subSecondPrecision_setAndGet() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setSubSecondPrecision(10);
        assertEquals(10, tq.getSubSecondPrecision());
    }

    @Test
    @DisplayName("sub-second precision max valid value")
    void subSecondPrecision_maxValue() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setSubSecondPrecision(24);
        assertEquals(24, tq.getSubSecondPrecision());
    }

    @Test
    @DisplayName("sub-second precision does not affect flags")
    void subSecondPrecision_doesNotAffectFlags() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setBit(LEAP_SECOND_KNOWN, true);
        tq.setSubSecondPrecision(5);
        assertTrue(tq.testBit(LEAP_SECOND_KNOWN));
        assertFalse(tq.testBit(CLOCK_FAULT));
        assertEquals(5, tq.getSubSecondPrecision());
    }

    @Test
    @DisplayName("construct from raw value")
    void construct_fromRaw() {
        // bit0=leapSecondKnown, bit1=clockFault, bits3~7=precision=24(0b11000)
        // raw = 0b11000_011 = 0xC3
        CmsTimeQuality tq = new CmsTimeQuality(0xC3);
        assertTrue(tq.testBit(LEAP_SECOND_KNOWN));
        assertTrue(tq.testBit(CLOCK_FAULT));
        assertFalse(tq.testBit(CLOCK_NOT_SYNCED));
        assertEquals(24, tq.getSubSecondPrecision());
    }

    @Test
    @DisplayName("encode and decode roundtrip")
    void encodeDecode_roundtrip() throws Exception {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setBit(CLOCK_NOT_SYNCED, true);
        tq.setSubSecondPrecision(3);

        PerOutputStream pos = new PerOutputStream();
        tq.encode(pos);

        CmsTimeQuality result = new CmsTimeQuality().decode(new PerInputStream(pos.toByteArray()));

        assertFalse(result.testBit(LEAP_SECOND_KNOWN));
        assertFalse(result.testBit(CLOCK_FAULT));
        assertTrue(result.testBit(CLOCK_NOT_SYNCED));
        assertEquals(3, result.getSubSecondPrecision());
        assertEquals(tq.get(), result.get());
    }

    @Test
    @DisplayName("setBit out of range throws")
    void setBit_outOfRange_throws() {
        CmsTimeQuality tq = new CmsTimeQuality();
        assertThrows(IllegalArgumentException.class, () -> tq.setBit(-1, true));
        assertThrows(IllegalArgumentException.class, () -> tq.setBit(8, true));
    }

    @Test
    @DisplayName("static write and read")
    void staticWriteRead() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsTimeQuality.write(pos, 0xC3L); // bit0, bit1, bits3~7=24

        CmsTimeQuality decoded = CmsTimeQuality.read(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.testBit(LEAP_SECOND_KNOWN));
        assertTrue(decoded.testBit(CLOCK_FAULT));
        assertFalse(decoded.testBit(CLOCK_NOT_SYNCED));
        assertEquals(24, decoded.getSubSecondPrecision());
    }

    @Test
    @DisplayName("raw value roundtrip")
    void raw_roundtrip() {
        CmsTimeQuality tq = new CmsTimeQuality();
        tq.setBit(LEAP_SECOND_KNOWN, true);
        tq.setSubSecondPrecision(10);
        long raw = tq.get();
        CmsTimeQuality tq2 = new CmsTimeQuality(raw);
        assertEquals(raw, tq2.get());
        assertTrue(tq2.testBit(LEAP_SECOND_KNOWN));
        assertEquals(10, tq2.getSubSecondPrecision());
    }
}