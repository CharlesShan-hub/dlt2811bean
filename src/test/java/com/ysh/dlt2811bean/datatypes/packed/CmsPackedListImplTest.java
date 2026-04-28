package com.ysh.dlt2811bean.datatypes.packed;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsPackedListImpl")
class CmsPackedListImplTest {

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsPackedListImpl list = new CmsPackedListImpl(8);
        assertEquals(0L, list.get());
        assertEquals(8, list.getMax());
        assertEquals(0, list.getBitLength());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsPackedListImpl list = new CmsPackedListImpl(0b00000101L, 3, 8);
        assertEquals(0b00000101L, list.get());
        assertEquals(3, list.getBitLength());
    }

    @Test
    @DisplayName("setBit and testBit")
    void setBitAndTestBit() {
        CmsPackedListImpl list = new CmsPackedListImpl(8);
        list.setBit(0, true).setBit(2, true);
        assertTrue(list.testBit(0));
        assertFalse(list.testBit(1));
        assertTrue(list.testBit(2));
        assertEquals(3, list.getBitLength());
    }

    @Test
    @DisplayName("setBit clears bit")
    void setBitClear() {
        CmsPackedListImpl list = new CmsPackedListImpl(0b00000101L, 3, 8);
        list.setBit(0, false);
        assertFalse(list.testBit(0));
        assertTrue(list.testBit(2));
    }

    @Test
    @DisplayName("setBits and getBits")
    void setBitsAndGetBits() {
        CmsPackedListImpl list = new CmsPackedListImpl(8);
        list.setBits(0, 2, 0b11);
        assertEquals(0b11, list.getBits(0, 2));
        assertEquals(2, list.getBitLength());
    }

    @Test
    @DisplayName("testBits")
    void testBits() {
        CmsPackedListImpl list = new CmsPackedListImpl(0b00000101L, 3, 8);
        assertTrue(list.testBits(0, 2, 0b01));
        assertFalse(list.testBits(0, 2, 0b10));
    }

    @Test
    @DisplayName("set overrides value")
    void set() {
        CmsPackedListImpl list = new CmsPackedListImpl(8);
        list.set(0b1010L);
        assertEquals(0b1010L, list.get());
    }

    @Test
    @DisplayName("set negative value throws")
    void setNegativeThrows() {
        CmsPackedListImpl list = new CmsPackedListImpl(8);
        assertThrows(IllegalArgumentException.class, () -> list.set(-1L));
    }

    @Test
    @DisplayName("encode/decode roundtrip")
    void encodeDecodeRoundtrip() throws Exception {
        CmsPackedListImpl list = new CmsPackedListImpl(0b00000101L, 3, 8);

        PerOutputStream pos = new PerOutputStream();
        list.encode(pos);

        CmsPackedListImpl decoded = new CmsPackedListImpl(8).decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0b00000101L, decoded.get());
        // bitLength is byte-aligned after decode (Java byte[] limitation)
        assertEquals(8, decoded.getBitLength());
    }

    @Test
    @DisplayName("encode/decode zero value")
    void encodeDecodeZero() throws Exception {
        CmsPackedListImpl list = new CmsPackedListImpl(8);

        PerOutputStream pos = new PerOutputStream();
        list.encode(pos);

        CmsPackedListImpl decoded = new CmsPackedListImpl(8).decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0L, decoded.get());
        assertEquals(0, decoded.getBitLength());
    }

    @Test
    @DisplayName("encode/decode max bits")
    void encodeDecodeMaxBits() throws Exception {
        CmsPackedListImpl list = new CmsPackedListImpl(0xABCDL, 16, 16);

        PerOutputStream pos = new PerOutputStream();
        list.encode(pos);

        CmsPackedListImpl decoded = new CmsPackedListImpl(16).decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0xABCDL, decoded.get());
        assertEquals(16, decoded.getBitLength());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsPackedListImpl original = new CmsPackedListImpl(0b00000101L, 3, 8);
        CmsPackedListImpl cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertEquals(original.getBitLength(), cloned.getBitLength());
        assertEquals(original.getMax(), cloned.getMax());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is independent")
    void copyIsIndependent() {
        CmsPackedListImpl original = new CmsPackedListImpl(0b00000101L, 3, 8);
        CmsPackedListImpl cloned = original.copy();
        cloned.set(0b00000011L);
        assertEquals(0b00000101L, original.get());
        assertEquals(0b00000011L, cloned.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsPackedListImpl list = new CmsPackedListImpl(0b00000101L, 3, 8);
        assertTrue(list.toString().contains("CmsPackedListImpl"));
    }
}