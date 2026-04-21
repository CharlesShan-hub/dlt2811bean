package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerOctetString")
class PerOctetStringTest {

    // ==================== Fixed-size ====================

    @Test
    @DisplayName("fixed-size: 64 bytes (associationId)")
    void fixedSize_64() throws PerDecodeException {
        byte[] data = new byte[64];
        Arrays.fill(data, (byte) 0xAB);

        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeFixedSize(pos, data, 64);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeFixedSize(pis, 64);
        assertArrayEquals(data, result);
        assertEquals(64, result.length);
    }

    @Test
    @DisplayName("fixed-size: 1 byte")
    void fixedSize_1() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeFixedSize(pos, new byte[]{0x42}, 1);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeFixedSize(pis, 1);
        assertArrayEquals(new byte[]{0x42}, result);
    }

    @Test
    @DisplayName("fixed-size: 0 bytes")
    void fixedSize_0() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeFixedSize(pos, new byte[0], 0);
        assertEquals(0, pos.getBitLength());

        PerInputStream pis = new PerInputStream(new byte[0]);
        byte[] result = PerOctetString.decodeFixedSize(pis, 0);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("fixed-size: short data padded with zeros")
    void fixedSize_padding() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeFixedSize(pos, new byte[]{0x01, 0x02}, 4);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeFixedSize(pis, 4);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x00, 0x00}, result);
    }

    // ==================== Constrained ====================

    @Test
    @DisplayName("constrained: 3 bytes in range 0..8192")
    void constrained_3bytes() throws PerDecodeException {
        byte[] data = {0x01, 0x02, 0x03};

        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeConstrained(pos, data, 0, 8192);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeConstrained(pis, 0, 8192);
        assertArrayEquals(data, result);
    }

    @Test
    @DisplayName("constrained: empty array with lb=0")
    void constrained_empty() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeConstrained(pos, new byte[0], 0, 100);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeConstrained(pis, 0, 100);
        assertEquals(0, result.length);
    }

    @Test
    @DisplayName("constrained: out of range throws")
    void constrained_outOfRange() {
        PerOutputStream pos = new PerOutputStream();
        byte[] data = new byte[100];
        assertThrows(IllegalArgumentException.class,
            () -> PerOctetString.encodeConstrained(pos, data, 0, 99));
    }

    // ==================== Unconstrained ====================

    @Test
    @DisplayName("unconstrained: string data")
    void unconstrained() throws PerDecodeException {
        byte[] data = "Hello DL/T 2811".getBytes();

        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeUnconstrained(pos, data);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeUnconstrained(pis);
        assertArrayEquals(data, result);
    }

    @Test
    @DisplayName("unconstrained: empty array")
    void unconstrained_empty() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerOctetString.encodeUnconstrained(pos, new byte[0]);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerOctetString.decodeUnconstrained(pis);
        assertEquals(0, result.length);
    }

    // ==================== Mixed ====================

    @Test
    @DisplayName("mixed: uint16 + octetString(3)")
    void mixed() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerInteger.encodeUint16(pos, 100);
        PerOctetString.encodeFixedSize(pos, new byte[]{(byte) 0xAA, (byte) 0xBB, (byte) 0xCC}, 3);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(100, PerInteger.decodeUint16(pis));
        byte[] result = PerOctetString.decodeFixedSize(pis, 3);
        assertArrayEquals(new byte[]{(byte) 0xAA, (byte) 0xBB, (byte) 0xCC}, result);
    }
}
