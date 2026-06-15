package com.ysh.dlt2811bean.per.types;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerReal")
class PerRealTest {

    // ==================== Float64 (double) ====================

    @Test
    @DisplayName("float64: positive value")
    void float64_positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat64(pos, 220.5);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(220.5, PerReal.decodeFloat64(pis));
    }

    @Test
    @DisplayName("float64: negative value")
    void float64_negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat64(pos, -3.14);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-3.14, PerReal.decodeFloat64(pis), 1e-10);
    }

    @Test
    @DisplayName("float64: zero")
    void float64_zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat64(pos, 0.0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0.0, PerReal.decodeFloat64(pis));
    }

    @Test
    @DisplayName("float64: zero uses only 1 bit")
    void float64_zero_oneBit() {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat64(pos, 0.0);
        assertEquals(1, pos.getBitLength());
    }

    @Test
    @DisplayName("float64: non-zero uses 1 bit + 8 bytes (aligned)")
    void float64_nonZero_size() {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat64(pos, 1.0);
        // 1 bit (flag) + align to byte (7 pad) + 8 bytes = 72 bits
        assertEquals(72, pos.getBitLength());
    }

    @Test
    @DisplayName("float64: very large value")
    void float64_large() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat64(pos, 1.7976931348623157E308);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1.7976931348623157E308, PerReal.decodeFloat64(pis), 0);
    }

    // ==================== Float32 (float) ====================

    @Test
    @DisplayName("float32: positive value")
    void float32_positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat32(pos, 3.14f);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(3.14f, PerReal.decodeFloat32(pis), 0.001f);
    }

    @Test
    @DisplayName("float32: negative value")
    void float32_negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat32(pos, -100.5f);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-100.5f, PerReal.decodeFloat32(pis), 0.001f);
    }

    @Test
    @DisplayName("float32: zero")
    void float32_zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat32(pos, 0.0f);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0.0f, PerReal.decodeFloat32(pis));
    }

    @Test
    @DisplayName("float32: zero uses only 1 bit")
    void float32_zero_oneBit() {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat32(pos, 0.0f);
        assertEquals(1, pos.getBitLength());
    }

    @Test
    @DisplayName("float32: non-zero uses 1 bit + 4 bytes (aligned)")
    void float32_nonZero_size() {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat32(pos, 1.0f);
        // 1 bit (flag) + align to byte (7 pad) + 4 bytes = 40 bits
        assertEquals(40, pos.getBitLength());
    }

    // ==================== Mixed ====================

    @Test
    @DisplayName("mixed: float32 + boolean + float64")
    void mixed() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerReal.encodeFloat32(pos, 1.234f);
        PerBoolean.encode(pos, true);
        PerReal.encodeFloat64(pos, 220.5);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1.234f, PerReal.decodeFloat32(pis), 0.001f);
        assertTrue(PerBoolean.decode(pis));
        assertEquals(220.5, PerReal.decodeFloat64(pis));
    }
}
