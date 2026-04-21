package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsUtcTimeTest {

    @Test
    void encodeDecode_basic() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(1715000000L, 1234567, 0x20);

        PerOutputStream pos = new PerOutputStream();
        CmsUtcTime.encode(pos, utc);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        CmsUtcTime result = CmsUtcTime.decode(pis);

        assertEquals(utc.getSecondsSinceEpoch(), result.getSecondsSinceEpoch());
        assertEquals(utc.getFractionOfSecond(), result.getFractionOfSecond());
        assertEquals(utc.getTimeQuality(), result.getTimeQuality());
    }

    @Test
    void encodeDecode_zeros() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(0, 0, 0);

        PerOutputStream pos = new PerOutputStream();
        CmsUtcTime.encode(pos, utc);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        CmsUtcTime result = CmsUtcTime.decode(pis);

        assertEquals(0, result.getSecondsSinceEpoch());
        assertEquals(0, result.getFractionOfSecond());
        assertEquals(0, result.getTimeQuality());
    }

    @Test
    void encodeDecode_maxValues() throws Exception {
        CmsUtcTime utc = new CmsUtcTime(0xFFFFFFFFL, 0xFFFFFF, 0xFF);

        PerOutputStream pos = new PerOutputStream();
        CmsUtcTime.encode(pos, utc);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        CmsUtcTime result = CmsUtcTime.decode(pis);

        assertEquals(0xFFFFFFFFL, result.getSecondsSinceEpoch());
        assertEquals(0xFFFFFF, result.getFractionOfSecond());
        assertEquals(0xFF, result.getTimeQuality());
    }

    @Test
    void encode_is8Bytes() {
        CmsUtcTime utc = new CmsUtcTime(1000, 0, 0);

        PerOutputStream pos = new PerOutputStream();
        CmsUtcTime.encode(pos, utc);

        assertEquals(8, pos.toByteArray().length);
    }

    @Test
    void constructor_rejectsInvalidSeconds() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(-1, 0, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0x100000000L, 0, 0));
    }

    @Test
    void constructor_rejectsInvalidFraction() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0, -1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0, 0x1000000, 0));
    }

    @Test
    void constructor_rejectsInvalidQuality() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0, 0, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsUtcTime(0, 0, 0x100));
    }
}
