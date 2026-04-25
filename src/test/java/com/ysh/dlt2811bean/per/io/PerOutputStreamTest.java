package com.ysh.dlt2811bean.per.io;

import com.ysh.dlt2811bean.per.exception.PerDecodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PerOutputStream")
class PerOutputStreamTest {

    @Test
    @DisplayName("writeBit: true sets bit")
    void writeBit_true() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true);
        byte[] data = pos.toByteArray();
        assertEquals(1, data.length);
        assertEquals((byte) 0x80, data[0]); // MSB set
    }

    @Test
    @DisplayName("writeBit: false does not set bit")
    void writeBit_false() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(false);
        byte[] data = pos.toByteArray();
        assertEquals(1, data.length);
        assertEquals((byte) 0x00, data[0]);
    }

    @Test
    @DisplayName("writeBits: multi-bit value")
    void writeBits_multi() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBits(0b1101, 4);
        byte[] data = pos.toByteArray();
        assertEquals(1, data.length);
        assertEquals((byte) 0xD0, data[0]); // 1101_0000
    }

    @Test
    @DisplayName("writeBits: 8 bits = 1 byte")
    void writeBits_8() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBits(0xFF, 8);
        assertEquals(8, pos.getBitLength());
        assertEquals(1, pos.getByteLength());
    }

    @Test
    @DisplayName("writeBits: value overflow throws")
    void writeBits_overflow() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> pos.writeBits(5, 2)); // 5 needs 3 bits
    }

    @Test
    @DisplayName("writeBits: negative value throws")
    void writeBits_negative() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class,
            () -> pos.writeBits(-1, 8));
    }

    @Test
    @DisplayName("writeBits: invalid numBits throws")
    void writeBits_invalidNumBits() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> pos.writeBits(0, 0));
        assertThrows(IllegalArgumentException.class, () -> pos.writeBits(0, 65));
    }

    @Test
    @DisplayName("align: pads to byte boundary")
    void align_padding() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true); // 1 bit
        pos.align();
        assertEquals(8, pos.getBitLength());
        assertTrue(pos.isAligned());
    }

    @Test
    @DisplayName("align: no-op when already aligned")
    void align_noOp() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBits(0xFF, 8);
        int before = pos.getBitLength();
        pos.align();
        assertEquals(before, pos.getBitLength());
    }

    @Test
    @DisplayName("writeByteAligned: auto-aligns before write")
    void writeByteAligned_autoAlign() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true); // 1 bit, not aligned
        pos.writeByteAligned((byte) 0x42);
        // After align: 8 bits, then 8 more = 16 bits
        assertEquals(16, pos.getBitLength());
        byte[] data = pos.toByteArray();
        assertEquals(0x42, data[1]);
    }

    @Test
    @DisplayName("writeBytes: raw bytes after alignment")
    void writeBytes() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true); // 1 bit offset
        byte[] input = {0x01, 0x02, 0x03};
        pos.writeBytes(input);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertTrue(pis.readBit());
        byte[] result = pis.readBytes(3);
        assertArrayEquals(input, result);
    }

    @Test
    @DisplayName("writeBytes: empty array does nothing")
    void writeBytes_empty() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBytes(new byte[0]);
        assertEquals(0, pos.getBitLength());
    }

    @Test
    @DisplayName("writeBytes: null does nothing")
    void writeBytes_null() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBytes(null);
        assertEquals(0, pos.getBitLength());
    }

    @Test
    @DisplayName("toByteArray: returns trimmed copy")
    void toByteArray() {
        PerOutputStream pos = new PerOutputStream(256); // large initial
        pos.writeBits(0xFF, 8);
        byte[] data = pos.toByteArray();
        assertEquals(1, data.length);
    }

    @Test
    @DisplayName("reset: clears all data")
    void reset() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBits(0xFF, 8);
        pos.reset();
        assertEquals(0, pos.getBitLength());
        assertTrue(pos.isAligned());
    }

    @Test
    @DisplayName("auto-expansion: writes beyond initial capacity")
    void autoExpansion() {
        PerOutputStream pos = new PerOutputStream(4);
        byte[] input = new byte[100];
        java.util.Arrays.fill(input, (byte) 0xAB);
        pos.writeBytes(input);

        byte[] data = pos.toByteArray();
        assertEquals(100, data.length);
        for (byte b : data) {
            assertEquals((byte) 0xAB, b);
        }
    }

    @Test
    @DisplayName("getBitLength and getByteLength accuracy")
    void lengthTracking() {
        PerOutputStream pos = new PerOutputStream();
        pos.writeBit(true);   // 1 bit
        assertEquals(1, pos.getBitLength());
        assertEquals(1, pos.getByteLength());

        pos.writeBit(true);   // 2 bits
        assertEquals(2, pos.getBitLength());
        assertEquals(1, pos.getByteLength());

        pos.writeBits(0xFF, 8); // 10 bits
        assertEquals(10, pos.getBitLength());
        assertEquals(2, pos.getByteLength());
    }

    @Test
    @DisplayName("constructor: invalid capacity throws")
    void constructor_invalidCapacity() {
        assertThrows(IllegalArgumentException.class,
            () -> new PerOutputStream(0));
        assertThrows(IllegalArgumentException.class,
            () -> new PerOutputStream(-1));
    }
}
