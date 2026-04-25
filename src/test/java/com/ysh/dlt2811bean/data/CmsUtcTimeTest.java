package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.compound.CmsUtcTime;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtcTime")
class CmsUtcTimeTest {

    @Test
    @DisplayName("default constructor sets all fields to zero")
    void default_isAllZero() {
        CmsUtcTime utc = new CmsUtcTime();
        assertEquals(0L, utc.secondsSinceEpoch.get());
        assertEquals(0, utc.fractionOfSecond.get());
        assertEquals(0L, utc.timeQuality.get());
    }

    @Test
    @DisplayName("full constructor with raw values")
    void fullConstructor_withRawValues() {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0xC3L);
        assertEquals(1715000000L, utc.secondsSinceEpoch.get());
        assertEquals(1234567, utc.fractionOfSecond.get());
        assertEquals(0xC3L, utc.timeQuality.get());
    }

    @Test
    @DisplayName("chain setters via public fields")
    void setters_chain() {
        CmsUtcTime utc = new CmsUtcTime();
        utc.secondsSinceEpoch.set(100L);
        utc.fractionOfSecond.set(500);
        utc.timeQuality.set(0x07L);
        assertEquals(100L, utc.secondsSinceEpoch.get());
        assertEquals(500, utc.fractionOfSecond.get());
        assertEquals(0x07L, utc.timeQuality.get());
    }

    @Test
    @DisplayName("encode and decode zeros")
    void encodeDecode_zeros() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(0, 0, 0L);

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        CmsUtcTime result = new CmsUtcTime().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(0L, result.secondsSinceEpoch.get());
        assertEquals(0, result.fractionOfSecond.get());
        assertEquals(0L, result.timeQuality.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsUtcTime original = new CmsUtcTime(1715000000L, 1234567, 0xC3L);
        CmsUtcTime cloned = original.copy();
        assertEquals(original.secondsSinceEpoch.get(), cloned.secondsSinceEpoch.get());
        assertEquals(original.fractionOfSecond.get(), cloned.fractionOfSecond.get());
        assertEquals(original.timeQuality.get(), cloned.timeQuality.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        CmsUtcTime original = new CmsUtcTime(1715000000L, 1234567, 0xC3L);
        CmsUtcTime cloned = original.copy();
        cloned.secondsSinceEpoch.set(999L);
        assertEquals(1715000000L, original.secondsSinceEpoch.get());
    }

    @Test
    @DisplayName("encode and decode max values")
    void encodeDecode_maxValues() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(0xFFFFFFFFL, 0xFFFFFF, 0xFFL);

        PerOutputStream pos = new PerOutputStream();
        utc.encode(pos);

        CmsUtcTime result = new CmsUtcTime().decode(new PerInputStream(pos.toByteArray()));

        assertEquals(0xFFFFFFFFL, result.secondsSinceEpoch.get());
        assertEquals(0xFFFFFF, result.fractionOfSecond.get());
        assertEquals(0xFFL, result.timeQuality.get());
    }
}