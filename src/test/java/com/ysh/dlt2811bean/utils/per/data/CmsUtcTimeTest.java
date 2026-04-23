package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ysh.dlt2811bean.utils.per.data.CmsTimeQuality.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtcTime")
class CmsUtcTimeTest {

    @Test
    @DisplayName("default constructor sets all fields to zero")
    void default_isAllZero() {
        CmsUtcTime utc = new CmsUtcTime();
        assertEquals(0L, utc.getSecondsSinceEpoch());
        assertEquals(0, utc.getFractionOfSecond());
        assertEquals(0L, utc.getTimeQuality().get());
    }

    @Test
    @DisplayName("convenience constructor with raw timeQuality")
    void convenienceConstructor_withRawTimeQuality() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20);
        assertEquals(1715000000L, utc.getSecondsSinceEpoch());
        assertEquals(1234567, utc.getFractionOfSecond());
        assertEquals(0x20L, utc.getTimeQuality().get());
    }

    @Test
    @DisplayName("full constructor with CmsTimeQuality")
    void fullConstructor_withTimeQuality() {
        CmsTimeQuality tq = new CmsTimeQuality(0xC3);
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, tq);
        assertEquals(1715000000L, utc.getSecondsSinceEpoch());
        assertEquals(1234567, utc.getFractionOfSecond());
        assertEquals(tq.get(), utc.getTimeQuality().get());
    }

    @Test
    @DisplayName("setters return this for chaining")
    void setters_chain() {
        CmsUtcTime utc = new CmsUtcTime()
                .setSecondsSinceEpoch(100L)
                .setFractionOfSecond(500)
                .setTimeQuality(new CmsTimeQuality(0x07));
        assertEquals(100L, utc.getSecondsSinceEpoch());
        assertEquals(500, utc.getFractionOfSecond());
        assertEquals(0x07L, utc.getTimeQuality().get());
    }

    @Test
    @DisplayName("setTimeQuality with raw int")
    void setTimeQuality_rawInt() {
        CmsUtcTime utc = new CmsUtcTime();
        utc.setTimeQuality(0xFF);
        assertEquals(0xFFL, utc.getTimeQuality().get());
    }

    @Test
    @DisplayName("encode and decode roundtrip")
    void encodeDecode_roundtrip() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20);

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        CmsUtcTime result = new CmsUtcTime().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(utc.getSecondsSinceEpoch(), result.getSecondsSinceEpoch());
        assertEquals(utc.getFractionOfSecond(), result.getFractionOfSecond());
        assertEquals(utc.getTimeQuality().get(), result.getTimeQuality().get());
    }

    @Test
    @DisplayName("encode and decode zeros")
    void encodeDecode_zeros() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(0, 0, new CmsTimeQuality());

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        CmsUtcTime result = new CmsUtcTime().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(0L, result.getSecondsSinceEpoch());
        assertEquals(0, result.getFractionOfSecond());
        assertEquals(0L, result.getTimeQuality().get());
    }

    @Test
    @DisplayName("encode and decode max values")
    void encodeDecode_maxValues() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(0xFFFFFFFFL, 0xFFFFFF, 0xFF);

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        CmsUtcTime result = new CmsUtcTime().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(0xFFFFFFFFL, result.getSecondsSinceEpoch());
        assertEquals(0xFFFFFF, result.getFractionOfSecond());
        assertEquals(0xFFL, result.getTimeQuality().get());
    }

    @Test
    @DisplayName("encode and decode with time quality flags")
    void encodeDecode_timeQualityFlags() throws Exception {
        CmsTimeQuality tq = new CmsTimeQuality()
                .setBit(LEAP_SECOND_KNOWN, true)
                .setBit(CLOCK_FAULT, true)
                .setSubSecondPrecision(24);

        CmsUtcTime utc = new CmsUtcTime(1715000000L, 0, tq);

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        CmsUtcTime result = new CmsUtcTime().decode(new PerInputStream(pos.toByteArray()));

        assertTrue(result.getTimeQuality().testBit(LEAP_SECOND_KNOWN));
        assertTrue(result.getTimeQuality().testBit(CLOCK_FAULT));
        assertFalse(result.getTimeQuality().testBit(CLOCK_NOT_SYNCED));
        assertEquals(24, result.getTimeQuality().getSubSecondPrecision());
    }

    @Test
    @DisplayName("encode produces exactly 8 bytes")
    void encode_is8Bytes() {
        CmsUtcTime utc = new CmsUtcTime(1000, 0, new CmsTimeQuality());

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        assertEquals(8, pos.toByteArray().length);
    }

    @Test
    @DisplayName("static write and read roundtrip")
    void static_writeRead_roundtrip() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20);

        PerOutputStream pos = new PerOutputStream();
        CmsUtcTime.write(pos, utc);

        CmsUtcTime result = CmsUtcTime.read(new PerInputStream(pos.toByteArray()));

        assertEquals(utc.getSecondsSinceEpoch(), result.getSecondsSinceEpoch());
        assertEquals(utc.getFractionOfSecond(), result.getFractionOfSecond());
        assertEquals(utc.getTimeQuality().get(), result.getTimeQuality().get());
    }

    @Test
    @DisplayName("static write with null encodes default")
    void static_write_null() {
        PerOutputStream pos = new PerOutputStream();
        CmsUtcTime.write(pos, null);
        assertEquals(8, pos.toByteArray().length);
    }

    @Test
    @DisplayName("setSecondsSinceEpoch rejects out of range")
    void setSecondsSinceEpoch_rejectsOutOfRange() {
        CmsUtcTime utc = new CmsUtcTime();
        assertThrows(IllegalArgumentException.class, () -> utc.setSecondsSinceEpoch(-1));
        assertThrows(IllegalArgumentException.class, () -> utc.setSecondsSinceEpoch(0x100000000L));
    }

    @Test
    @DisplayName("setFractionOfSecond rejects out of range")
    void setFractionOfSecond_rejectsOutOfRange() {
        CmsUtcTime utc = new CmsUtcTime();
        assertThrows(IllegalArgumentException.class, () -> utc.setFractionOfSecond(-1));
        assertThrows(IllegalArgumentException.class, () -> utc.setFractionOfSecond(0x1000000));
    }

    @Test
    @DisplayName("constructor rejects invalid seconds")
    void constructor_rejectsInvalidSeconds() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(-1, 0, new CmsTimeQuality()));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0x100000000L, 0, new CmsTimeQuality()));
    }

    @Test
    @DisplayName("constructor rejects invalid fraction")
    void constructor_rejectsInvalidFraction() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0, -1, new CmsTimeQuality()));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0, 0x1000000, new CmsTimeQuality()));
    }
}