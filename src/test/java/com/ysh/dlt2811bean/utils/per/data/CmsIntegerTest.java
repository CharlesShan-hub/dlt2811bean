package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt* — DL/T 2811 typed integer beans")
class CmsIntegerTest {

    // ==================== Unsigned ====================

    @Test
    @DisplayName("INT8U round-trip")
    void uint8() throws PerDecodeException {
        for (int v : new int[]{0, 1, 127, 128, 255}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt8U.encode(pos, new CmsInt8U(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt8U r = CmsInt8U.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT8U out of range throws")
    void uint8_out_of_range() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8U(256));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8U(-1));
    }

    @Test
    @DisplayName("INT16U round-trip")
    void uint16() throws PerDecodeException {
        for (int v : new int[]{0, 1, 255, 256, 65535}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt16U.encode(pos, new CmsInt16U(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt16U r = CmsInt16U.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT32U round-trip")
    void uint32() throws PerDecodeException {
        for (long v : new long[]{0, 255, 65536, 4294967295L}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt32U.encode(pos, new CmsInt32U(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt32U r = CmsInt32U.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT64U round-trip")
    void uint64() throws PerDecodeException {
        for (long v : new long[]{0, 255, 65536, Long.MAX_VALUE}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64U.encode(pos, new CmsInt64U(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt64U r = CmsInt64U.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT64U negative value throws")
    void uint64_negative_throws() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt64U(-1L));
    }

    // ==================== Signed ====================

    @Test
    @DisplayName("INT8 round-trip")
    void int8() throws PerDecodeException {
        for (int v : new int[]{-128, -1, 0, 1, 127}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt8.encode(pos, new CmsInt8(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt8 r = CmsInt8.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT8 out of range throws")
    void int8_out_of_range() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8(128));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8(-129));
    }

    @Test
    @DisplayName("INT16 round-trip")
    void int16() throws PerDecodeException {
        for (int v : new int[]{-32768, -1, 0, 32767}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt16.encode(pos, new CmsInt16(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt16 r = CmsInt16.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT32 round-trip")
    void int32() throws PerDecodeException {
        for (int v : new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt32.encode(pos, new CmsInt32(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt32 r = CmsInt32.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    @Test
    @DisplayName("INT64 round-trip")
    void int64() throws PerDecodeException {
        for (long v : new long[]{Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE}) {
            PerOutputStream pos = new PerOutputStream();
            CmsInt64.encode(pos, new CmsInt64(v));

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            CmsInt64 r = CmsInt64.decode(pis);
            assertEquals(v, r.getValue());
        }
    }

    // ==================== Bean features ====================

    @Test
    @DisplayName("bean: default constructor, setter chain, toString")
    void beanFeatures() {
        CmsInt8U val = new CmsInt8U().setValue(42);
        assertEquals(42, val.getValue());
        assertEquals("42", val.toString());

        CmsInt32U val32 = new CmsInt32U().setValue(100000L);
        assertEquals(100000L, val32.getValue());
    }

    // ==================== Mixed sequence (simulates Cms01 encoding) ====================

    @Test
    @DisplayName("mixed: int2 + uint8 + uint16 + uint16 (Cms01-like)")
    void mixedCms01() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        com.ysh.dlt2811bean.utils.per.types.PerOctetString.encodeInt2(pos, 42);
        CmsInt8U.encode(pos, new CmsInt8U(1));
        CmsInt16U.encode(pos, new CmsInt16U(65535));
        CmsInt16U.encode(pos, new CmsInt16U(4096));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(42, com.ysh.dlt2811bean.utils.per.types.PerOctetString.decodeInt2(pis));
        assertEquals(1, CmsInt8U.decode(pis).getValue());
        assertEquals(65535, CmsInt16U.decode(pis).getValue());
        assertEquals(4096, CmsInt16U.decode(pis).getValue());
    }
}
