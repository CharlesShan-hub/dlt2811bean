package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.string.CmsOctetString;
import com.ysh.dlt2811bean.data.type.AbstractCmsString;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOctetString")
class CmsOctetStringTest {

    @Test
    @DisplayName("constructor with byte array")
    void constructorWithByteArray() {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        assertArrayEquals(data, str.get());
    }

    @Test
    @DisplayName("constructor with null byte array")
    void constructorWithNullByteArray() {
        CmsOctetString str = new CmsOctetString(null);
        assertArrayEquals(new byte[0], str.get());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsOctetString str = new CmsOctetString();
        assertArrayEquals(new byte[0], str.get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsOctetString str = new CmsOctetString();
        byte[] data = {0x01, 0x02, 0x03};
        str.set(data);
        assertArrayEquals(data, str.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsOctetString().set(null));
    }

    @Test
    @DisplayName("size method")
    void size() {
        CmsOctetString str = new CmsOctetString(new byte[]{0x01, 0x02});
        str.size(10);
        assertEquals(10, str.getSize());
    }

    @Test
    @DisplayName("max method")
    void max() {
        CmsOctetString str = new CmsOctetString(new byte[]{0x01, 0x02});
        str.max(100);
        assertEquals(100, str.getMax());
    }

    @Test
    @DisplayName("size clears max")
    void sizeClearsMax() {
        CmsOctetString str = new CmsOctetString(new byte[]{0x01, 0x02});
        str.max(100);
        str.size(10);
        assertEquals(10, str.getSize());
        assertNull(str.getMax());
    }

    @Test
    @DisplayName("max clears size")
    void maxClearsSize() {
        CmsOctetString str = new CmsOctetString(new byte[]{0x01, 0x02});
        str.size(10);
        str.max(100);
        assertEquals(100, str.getMax());
        assertNull(str.getSize());
    }

    @Test
    @DisplayName("encode/decode fixed size")
    void encodeDecodeFixedSize() throws Exception {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        str.size(5);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = new CmsOctetString().size(5).decode(new PerInputStream(pos.toByteArray()));
        // Fixed size encoding pads with zeros, so we get 5 bytes
        byte[] expected = {0x01, 0x02, 0x03, 0x00, 0x00};
        assertArrayEquals(expected, decoded.get());
    }

    @Test
    @DisplayName("encode/decode variable size")
    void encodeDecodeVariableSize() throws Exception {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = new CmsOctetString().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(data, decoded.get());
    }

    @Test
    @DisplayName("encode/decode empty byte array")
    void encodeDecodeEmptyByteArray() throws Exception {
        CmsOctetString str = new CmsOctetString(new byte[0]);
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = new CmsOctetString().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(new byte[0], decoded.get());
    }

    @Test
    @DisplayName("encode/decode large byte array")
    void encodeDecodeLargeByteArray() throws Exception {
        byte[] data = new byte[50];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }
        CmsOctetString str = new CmsOctetString(data);
        str.max(255);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = new CmsOctetString().max(255).decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(data, decoded.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString()
            .set(data)
            .max(100);

        assertArrayEquals(data, str.get());
        assertEquals(100, str.getMax());

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = new CmsOctetString().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(data, decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        assertEquals("OCTET STRING: [01 02 03]", str.toString());
    }

    @Test
    @DisplayName("toString with empty array")
    void toStringWithEmptyArray() {
        CmsOctetString str = new CmsOctetString(new byte[0]);
        assertEquals("OCTET STRING: []", str.toString());
    }

    @Test
    @DisplayName("static read method with fixed size")
    void staticReadFixedSize() throws Exception {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        str.size(5);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = CmsOctetString.read(new PerInputStream(pos.toByteArray()), 
            AbstractCmsString.Mode.FIXED, 5);
        // Fixed size encoding pads with zeros, so we get 5 bytes
        byte[] expected = {0x01, 0x02, 0x03, 0x00, 0x00};
        assertArrayEquals(expected, decoded.get());
    }

    @Test
    @DisplayName("static read method with variable size")
    void staticReadVariableSize() throws Exception {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = CmsOctetString.read(new PerInputStream(pos.toByteArray()), 
            AbstractCmsString.Mode.VARIABLE, 100);
        assertArrayEquals(data, decoded.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString original = new CmsOctetString(data).max(100);
        CmsOctetString cloned = original.copy();
        assertArrayEquals(original.get(), cloned.get());
        assertEquals(original.getMax(), cloned.getMax());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is deep")
    void copyIsDeep() {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString original = new CmsOctetString(data).max(100);
        CmsOctetString cloned = original.copy();
        cloned.get()[0] = (byte) 0xFF;
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, original.get());
    }

    @Test
    @DisplayName("encode/decode with exact size match")
    void encodeDecodeWithExactSizeMatch() throws Exception {
        byte[] data = {0x01, 0x02, 0x03};
        CmsOctetString str = new CmsOctetString(data);
        str.size(3);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsOctetString decoded = new CmsOctetString().size(3).decode(new PerInputStream(pos.toByteArray()));
        assertArrayEquals(data, decoded.get());
    }
}