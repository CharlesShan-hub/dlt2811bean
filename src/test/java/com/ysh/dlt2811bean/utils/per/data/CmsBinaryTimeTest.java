package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CmsBinaryTimeTest {

    @Test
    void encodeDecode_basic() throws Exception {
        CmsBinaryTime bt = new CmsBinaryTime(12345678, 15000);

        PerOutputStream pos = new PerOutputStream();
        CmsBinaryTime.encode(pos, bt);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        CmsBinaryTime result = CmsBinaryTime.decode(pis);

        assertEquals(bt.getMsOfDay(), result.getMsOfDay());
        assertEquals(bt.getDaysSince1984(), result.getDaysSince1984());
    }

    @Test
    void encodeDecode_zeros() throws Exception {
        CmsBinaryTime bt = new CmsBinaryTime(0, 0);

        PerOutputStream pos = new PerOutputStream();
        CmsBinaryTime.encode(pos, bt);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        CmsBinaryTime result = CmsBinaryTime.decode(pis);

        assertEquals(0, result.getMsOfDay());
        assertEquals(0, result.getDaysSince1984());
    }

    @Test
    void encodeDecode_maxValues() throws Exception {
        // Max msOfDay for one day: 86399999 (23:59:59.999)
        CmsBinaryTime bt = new CmsBinaryTime(86399999, 65535);

        PerOutputStream pos = new PerOutputStream();
        CmsBinaryTime.encode(pos, bt);
        byte[] data = pos.toByteArray();

        PerInputStream pis = new PerInputStream(data);
        CmsBinaryTime result = CmsBinaryTime.decode(pis);

        assertEquals(86399999, result.getMsOfDay());
        assertEquals(65535, result.getDaysSince1984());
    }

    @Test
    void encode_is6Bytes() {
        CmsBinaryTime bt = new CmsBinaryTime(1000, 100);

        PerOutputStream pos = new PerOutputStream();
        CmsBinaryTime.encode(pos, bt);

        assertEquals(6, pos.toByteArray().length);
    }

    @Test
    void constructor_rejectsInvalidMsOfDay() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsBinaryTime(-1, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsBinaryTime(86400000, 0));
    }

    @Test
    void constructor_rejectsInvalidDays() {
        assertThrows(IllegalArgumentException.class,
                () -> new CmsBinaryTime(0, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new CmsBinaryTime(0, 65536));
    }
}
