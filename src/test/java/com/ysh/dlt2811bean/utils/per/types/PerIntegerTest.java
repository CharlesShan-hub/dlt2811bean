package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerInteger")
class PerIntegerTest {

    // ==================== Constrained encode/decode ====================

    @Test
    @DisplayName("constrained: range=1 (fixed value) — 0 bits")
    void constrained_fixedValue() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encode(pos, 42, 42, 42);
        assertEquals(0, pos.getBitLength());

        PerInputStream pis = new PerInputStream(new byte[0]);
        assertEquals(42, PerInteger.decode(pis, 42, 42));
    }

    @Test
    @DisplayName("constrained: range 0..7 (3 bits)")
    void constrained_3bits() throws PerDecodeException {
        for (int v = 0; v <= 7; v++) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encode(pos, v, 0, 7);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decode(pis, 0, 7));
        }
    }

    @Test
    @DisplayName("constrained: range 0..255 (8 bits)")
    void constrained_8bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encode(pos, 200, 0, 255);
        assertEquals(8, pos.getBitLength());

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(200, PerInteger.decode(pis, 0, 255));
    }

    @Test
    @DisplayName("constrained: range 0..65535 (16 bits, aligned)")
    void constrained_16bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encode(pos, 65535, 0, 65535);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(65535, PerInteger.decode(pis, 0, 65535));
    }

    @Test
    @DisplayName("constrained: range 0..65535 with bit offset (aligned to byte)")
    void constrained_16bits_withOffset() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true); // 1 bit offset
        PerInteger.encode(pos, 1000, 0, 65535);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(PerBoolean.decode(pis));
        assertEquals(1000, PerInteger.decode(pis, 0, 65535));
    }

    @Test
    @DisplayName("constrained: negative range -128..127")
    void constrained_negative() throws PerDecodeException {
        for (int v : new int[]{-128, -1, 0, 1, 127}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encode(pos, v, -128, 127);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decode(pis, -128, 127));
        }
    }

    @Test
    @DisplayName("constrained: value out of range throws")
    void constrained_outOfRange() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerInteger.encode(pos, 300, 0, 255));
    }

    // ==================== Typed convenience methods ====================

    @Test
    @DisplayName("Uint8 round-trip")
    void uint8() throws PerDecodeException {
        for (int v : new int[]{0, 1, 127, 128, 255}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeUint8(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeUint8(pis));
        }
    }

    @Test
    @DisplayName("Uint16 round-trip")
    void uint16() throws PerDecodeException {
        for (int v : new int[]{0, 1, 255, 256, 65535}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeUint16(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeUint16(pis));
        }
    }

    @Test
    @DisplayName("Uint32 round-trip")
    void uint32() throws PerDecodeException {
        for (long v : new long[]{0, 255, 65536, 4294967295L}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeUint32(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeUint32(pis));
        }
    }

    @Test
    @DisplayName("Uint64 round-trip")
    void uint64() throws PerDecodeException {
        for (long v : new long[]{0, 255, 65536, Long.MAX_VALUE}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeUint64(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeUint64(pis));
        }
    }

    @Test
    @DisplayName("Uint64 negative value throws")
    void uint64_negative_throws() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerInteger.encodeUint64(pos, -1L));
    }

    @Test
    @DisplayName("Int8 round-trip")
    void int8() throws PerDecodeException {
        for (int v : new int[]{-128, -1, 0, 1, 127}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeInt8(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeInt8(pis));
        }
    }

    @Test
    @DisplayName("Int16 round-trip")
    void int16() throws PerDecodeException {
        for (int v : new int[]{-32768, -1, 0, 32767}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeInt16(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeInt16(pis));
        }
    }

    @Test
    @DisplayName("Int32 round-trip")
    void int32() throws PerDecodeException {
        for (long v : new long[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeInt32(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals((int) v, PerInteger.decodeInt32(pis));
        }
    }

    @Test
    @DisplayName("Int64 round-trip")
    void int64() throws PerDecodeException {
        for (long v : new long[]{Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeInt64(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeInt64(pis));
        }
    }

    // ==================== Small non-negative ====================

    @Test
    @DisplayName("small non-negative: 0..63 (7 bits)")
    void smallNonNegative_small() throws PerDecodeException {
        for (int v = 0; v <= 63; v++) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeSmallNonNegative(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeSmallNonNegative(pis));
        }
    }

    @Test
    @DisplayName("small non-negative: >=64 (long form)")
    void smallNonNegative_large() throws PerDecodeException {
        for (int v : new int[]{64, 100, 1000, 10000}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeSmallNonNegative(pos, v);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(v, PerInteger.decodeSmallNonNegative(pis));
        }
    }

    // ==================== Unconstrained ====================

    @Test
    @DisplayName("unconstrained: positive value")
    void unconstrained_positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUnconstrained(pos, 12345);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(12345, PerInteger.decodeUnconstrained(pis));
    }

    @Test
    @DisplayName("unconstrained: negative value")
    void unconstrained_negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUnconstrained(pos, -100);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-100, PerInteger.decodeUnconstrained(pis));
    }

    @Test
    @DisplayName("unconstrained: zero")
    void unconstrained_zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUnconstrained(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, PerInteger.decodeUnconstrained(pis));
    }

    @Test
    @DisplayName("unconstrained: Long.MAX_VALUE")
    void unconstrained_maxLong() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUnconstrained(pos, Long.MAX_VALUE);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(Long.MAX_VALUE, PerInteger.decodeUnconstrained(pis));
    }

    @Test
    @DisplayName("unconstrained: Long.MIN_VALUE")
    void unconstrained_minLong() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUnconstrained(pos, Long.MIN_VALUE);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(Long.MIN_VALUE, PerInteger.decodeUnconstrained(pis));
    }

    // ==================== Semi-constrained ====================

    @Test
    @DisplayName("semi-constrained: positive with lower bound 0")
    void semiConstrained() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeSemiConstrained(pos, 999, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(999, PerInteger.decodeSemiConstrained(pis, 0));
    }

    // ==================== Length determinant ====================

    @Test
    @DisplayName("length: short form 0..127")
    void length_short() throws PerDecodeException {
        for (int len : new int[]{0, 1, 64, 127}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeLength(pos, len);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(len, PerInteger.decodeLength(pis));
        }
    }

    @Test
    @DisplayName("length: medium form 128..16383")
    void length_medium() throws PerDecodeException {
        for (int len : new int[]{128, 255, 1000, 16383}) {
            PerOutputStream pos = new PerOutputStream();
            PerInteger.encodeLength(pos, len);

            PerInputStream pis = new PerInputStream(pos.toByteArray());
            assertEquals(len, PerInteger.decodeLength(pis));
        }
    }

    @Test
    @DisplayName("length: negative throws")
    void length_negative_throws() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> PerInteger.encodeLength(pos, -1));
    }

    // ==================== Mixed types in sequence ====================

    @Test
    @DisplayName("mixed: boolean + uint8 + uint16 + int8")
    void mixedTypes() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true);
        PerInteger.encodeUint8(pos, 42);
        PerInteger.encodeUint16(pos, 1000);
        PerInteger.encodeInt8(pos, -50);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(PerBoolean.decode(pis));
        assertEquals(42, PerInteger.decodeUint8(pis));
        assertEquals(1000, PerInteger.decodeUint16(pis));
        assertEquals(-50, PerInteger.decodeInt8(pis));
    }
}
