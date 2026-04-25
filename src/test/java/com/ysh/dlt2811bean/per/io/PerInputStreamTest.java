package com.ysh.dlt2811bean.per.io;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerInputStream")
class PerInputStreamTest {

    @Test
    @DisplayName("readBit: MSB first")
    void readBit_msbFirst() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{(byte) 0x80});
        assertTrue(pis.readBit());
    }

    @Test
    @DisplayName("readBit: LSB")
    void readBit_lsb() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{(byte) 0x01});
        assertFalse(pis.readBit()); // bits 0-6 are 0
        assertFalse(pis.readBit());
        assertFalse(pis.readBit());
        assertFalse(pis.readBit());
        assertFalse(pis.readBit());
        assertFalse(pis.readBit());
        assertFalse(pis.readBit());
        assertTrue(pis.readBit());  // bit 7 is 1
    }

    @Test
    @DisplayName("readBits: multi-bit read")
    void readBits_multi() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{(byte) 0xD0}); // 1101_0000
        assertEquals(0b1101, pis.readBits(4));
    }

    @Test
    @DisplayName("readBits: 8 bits = 1 byte")
    void readBits_8() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x42});
        assertEquals(0x42, pis.readBits(8));
    }

    @Test
    @DisplayName("readBits: cross-byte boundary")
    void readBits_crossByte() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x0F, (byte) 0xF0});
        assertEquals(0x0FF, pis.readBits(12)); // 0000_1111_1111_0000
    }

    @Test
    @DisplayName("readBits: underflow throws")
    void readBits_underflow() {
        PerInputStream pis = new PerInputStream(new byte[]{0x01});
        assertThrows(PerDecodeException.class, () -> pis.readBits(9));
    }

    @Test
    @DisplayName("readByteAligned: auto-aligns first")
    void readByteAligned_autoAlign() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{(byte) 0x80, 0x42});
        pis.readBit(); // skip MSB, now at bit 1
        int val = pis.readByteAligned(); // align to bit 8, read 0x42
        assertEquals(0x42, val);
    }

    @Test
    @DisplayName("readBytes: raw bytes after alignment")
    void readBytes() throws PerDecodeException {
        byte[] data = {0x01, 0x02, 0x03, 0x04}; // use (byte) cast
        PerInputStream pis = new PerInputStream(data);
        byte[] result = pis.readBytes(3);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, result);
    }

    @Test
    @DisplayName("readSignedInteger: positive")
    void readSignedInteger_positive() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x00, (byte) 0x80}); // 128
        assertEquals(128, pis.readSignedInteger(2));
    }

    @Test
    @DisplayName("readSignedInteger: negative (sign-extend)")
    void readSignedInteger_negative() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{(byte) 0xFF, (byte) 0xFF}); // -1
        assertEquals(-1, pis.readSignedInteger(2));
    }

    @Test
    @DisplayName("readUnsignedInteger")
    void readUnsignedInteger() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x01, 0x00}); // 256
        assertEquals(256, pis.readUnsignedInteger(2));
    }

    @Test
    @DisplayName("align: skips to next byte boundary")
    void align_skip() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x01, 0x42});
        pis.readBit(); // bit 0
        pis.align();  // skip to bit 8
        assertEquals(8, pis.getBitPosition());
        assertEquals(0x42, pis.readByteAligned());
    }

    @Test
    @DisplayName("align: no-op when already aligned")
    void align_noOp() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x42});
        pis.align();
        assertEquals(0, pis.getBitPosition());
    }

    @Test
    @DisplayName("status: remaining bits tracking")
    void remainingTracking() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x01, 0x02});
        assertEquals(16, pis.getRemainingBits());
        assertEquals(2, pis.getRemainingBytes());
        assertTrue(pis.hasRemaining());

        pis.readBit();
        assertEquals(15, pis.getRemainingBits());
    }

    @Test
    @DisplayName("isAtEnd: true after consuming all data")
    void isAtEnd_allConsumed() throws PerDecodeException {
        PerInputStream pis = new PerInputStream(new byte[]{0x42});
        pis.readBits(8);
        assertFalse(pis.hasRemaining());
        assertTrue(pis.isAtEnd(false));
    }

    @Test
    @DisplayName("constructor: null throws")
    void constructor_null() {
        assertThrows(IllegalArgumentException.class,
            () -> new PerInputStream(null));
    }

    // ==================== Round-trip with PerOutputStream ====================

    @Test
    @DisplayName("round-trip: writeBit + readBit")
    void roundTrip_bit() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true);
        pos.writeBit(false);
        pos.writeBit(true);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(pis.readBit());
        assertFalse(pis.readBit());
        assertTrue(pis.readBit());
    }

    @Test
    @DisplayName("round-trip: complex mixed data")
    void roundTrip_complex() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true);
        pos.writeBits(0b1010, 4);
        pos.align();
        pos.writeBytes(new byte[]{(byte) 0xDE, (byte) 0xAD});
        pos.writeByteAligned((byte) 0xBE);

        byte[] data = pos.toByteArray();
        PerInputStream pis = new PerInputStream(data);

        assertTrue(pis.readBit());
        assertEquals(0b1010, pis.readBits(4));
        pis.align();
        byte[] bytes = pis.readBytes(2);
        assertArrayEquals(new byte[]{(byte) 0xDE, (byte) 0xAD}, bytes);
        assertEquals(0xBE, pis.readByteAligned());
        assertFalse(pis.hasRemaining());
    }
}
