package com.ysh.dlt2811bean.utils.per.types;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerBitString")
class PerBitStringTest {

    // ==================== Fixed-size (long) ====================

    @Test
    @DisplayName("fixed-size: 1 bit true")
    void fixedSize_1bit_true() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, 1, 1);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1, PerBitString.decodeFixedSize(pis, 1));
    }

    @Test
    @DisplayName("fixed-size: 1 bit false")
    void fixedSize_1bit_false() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, 0, 1);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, PerBitString.decodeFixedSize(pis, 1));
    }

    @Test
    @DisplayName("fixed-size: 6 bits (TriggerConditions)")
    void fixedSize_6bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, 0b000110, 6);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0b000110, PerBitString.decodeFixedSize(pis, 6));
    }

    @Test
    @DisplayName("fixed-size: 10 bits (RCBOptFlds)")
    void fixedSize_10bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, 0b0000011111, 10);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0b0000011111, PerBitString.decodeFixedSize(pis, 10));
    }

    @Test
    @DisplayName("fixed-size: 16 bits (boundary)")
    void fixedSize_16bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, 0xFFFF, 16);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0xFFFF, PerBitString.decodeFixedSize(pis, 16));
    }

    @Test
    @DisplayName("fixed-size: 0 bits")
    void fixedSize_0bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, 0, 0);
        assertEquals(0, pos.getBitLength());

        PerInputStream pis = new PerInputStream(new byte[0]);
        assertEquals(0, PerBitString.decodeFixedSize(pis, 0));
    }

    // ==================== Fixed-size (byte[]) ====================

    @Test
    @DisplayName("fixed-size byte[]: 6 bits")
    void fixedSize_bytes_6bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        byte[] data = {(byte) 0b00011000};
        PerBitString.encodeFixedSize(pos, data, 6);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerBitString.decodeFixedSizeBytes(pis, 6);
        assertEquals(1, result.length);
        assertEquals((byte) 0b00011000, result[0]);
    }

    @Test
    @DisplayName("fixed-size byte[]: 0 bits returns empty array")
    void fixedSize_bytes_0bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBitString.encodeFixedSize(pos, new byte[0], 0);

        PerInputStream pis = new PerInputStream(new byte[0]);
        byte[] result = PerBitString.decodeFixedSizeBytes(pis, 0);
        assertEquals(0, result.length);
    }

    // ==================== Constrained ====================

    @Test
    @DisplayName("constrained: 8 bits")
    void constrained_8bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        byte[] data = {(byte) 0xAB};
        PerBitString.encodeConstrained(pos, data, 8, 0, 65535);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerBitString.decodeConstrained(pis, 0, 65535);
        assertArrayEquals(data, result);
    }

    @Test
    @DisplayName("constrained: 13 bits")
    void constrained_13bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        byte[] data = {(byte) 0xAB, (byte) 0xCD};
        PerBitString.encodeConstrained(pos, data, 13, 0, 65535);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        byte[] result = PerBitString.decodeConstrained(pis, 0, 65535);
        assertArrayEquals(data, result);
    }

    // ==================== Unconstrained ====================

    @Test
    @DisplayName("unconstrained: 8 bits")
    void unconstrained_8bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        byte[] data = {(byte) 0xFF};
        PerBitString.encodeUnconstrained(pos, data, 8);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerBitString.BitStringResult result = PerBitString.decodeUnconstrained(pis);
        assertEquals(8, result.bitLength);
        assertArrayEquals(data, result.data);
    }

    @Test
    @DisplayName("unconstrained: 13 bits (non-byte-aligned)")
    void unconstrained_13bits() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        byte[] data = {(byte) 0xAB, (byte) 0xCD};
        PerBitString.encodeUnconstrained(pos, data, 13);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        PerBitString.BitStringResult result = PerBitString.decodeUnconstrained(pis);
        assertEquals(13, result.bitLength);
        assertArrayEquals(data, result.data);
    }

    // ==================== Combined with other types ====================

    @Test
    @DisplayName("mixed: boolean + bitstring(6) + uint8")
    void mixed() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        PerBoolean.encode(pos, true);
        PerBitString.encodeFixedSize(pos, 0b000110, 6);
        PerInteger.encodeUint8(pos, 42);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(PerBoolean.decode(pis));
        assertEquals(0b000110, PerBitString.decodeFixedSize(pis, 6));
        assertEquals(42, PerInteger.decodeUint8(pis));
    }
}
