package com.ysh.dlt2811bean.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsUtf8String;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsString;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsUtf8String")
class CmsUtf8StringTest {

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsUtf8String str = new CmsUtf8String("test");
        assertEquals("test", str.get());
    }

    @Test
    @DisplayName("constructor with unicode value")
    void constructorWithUnicodeValue() {
        CmsUtf8String str = new CmsUtf8String("设备名称");
        assertEquals("设备名称", str.get());
    }

    @Test
    @DisplayName("constructor with null value")
    void constructorWithNullValue() {
        CmsUtf8String str = new CmsUtf8String(null);
        assertEquals("", str.get());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsUtf8String str = new CmsUtf8String();
        assertEquals("", str.get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsUtf8String str = new CmsUtf8String();
        str.set("hello");
        assertEquals("hello", str.get());
    }

    @Test
    @DisplayName("set unicode string")
    void setUnicodeString() {
        CmsUtf8String str = new CmsUtf8String();
        str.set("设备名称");
        assertEquals("设备名称", str.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsUtf8String().set(null));
    }

    @Test
    @DisplayName("size method")
    void size() {
        CmsUtf8String str = new CmsUtf8String("test");
        str.size(10);
        assertEquals(10, str.getSize());
    }

    @Test
    @DisplayName("max method")
    void max() {
        CmsUtf8String str = new CmsUtf8String("test");
        str.max(100);
        assertEquals(100, str.getMax());
    }

    @Test
    @DisplayName("size clears max")
    void sizeClearsMax() {
        CmsUtf8String str = new CmsUtf8String("test");
        str.max(100);
        str.size(10);
        assertEquals(10, str.getSize());
        assertNull(str.getMax());
    }

    @Test
    @DisplayName("max clears size")
    void maxClearsSize() {
        CmsUtf8String str = new CmsUtf8String("test");
        str.size(10);
        str.max(100);
        assertEquals(100, str.getMax());
        assertNull(str.getSize());
    }

    @Test
    @DisplayName("bmp method")
    void bmp() {
        CmsUtf8String str = new CmsUtf8String("test");
        str.bmp(true);
        assertTrue(str.isBmp());
    }

    @Test
    @DisplayName("encode/decode fixed size with bmp mode")
    void encodeDecodeFixedSizeWithBmpMode() throws Exception {
        CmsUtf8String str = new CmsUtf8String("test");
        str.size(10);
        str.bmp(true);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().size(10).bmp(true).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
        assertTrue(decoded.isBmp());
    }

    @Test
    @DisplayName("encode/decode variable size")
    void encodeDecodeVariableSize() throws Exception {
        CmsUtf8String str = new CmsUtf8String("test");
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("encode/decode unicode string")
    void encodeDecodeUnicodeString() throws Exception {
        CmsUtf8String str = new CmsUtf8String("设备名称");
        str.max(255);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().max(255).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("设备名称", decoded.get());
    }

    @Test
    @DisplayName("encode/decode empty string")
    void encodeDecodeEmptyString() throws Exception {
        CmsUtf8String str = new CmsUtf8String("");
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("", decoded.get());
    }

    @Test
    @DisplayName("encode/decode with bmp mode")
    void encodeDecodeWithBmpMode() throws Exception {
        CmsUtf8String str = new CmsUtf8String("test");
        str.max(100);
        str.bmp(true);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().max(100).bmp(true).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
        assertTrue(decoded.isBmp());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsUtf8String str = new CmsUtf8String()
            .set("test")
            .max(100)
            .bmp(true);

        assertEquals("test", str.get());
        assertEquals(100, str.getMax());
        assertTrue(str.isBmp());

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().max(100).bmp(true).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsUtf8String str = new CmsUtf8String("test");
        assertEquals("UTF8String: test", str.toString());
    }

    @Test
    @DisplayName("toString with unicode")
    void toStringWithUnicode() {
        CmsUtf8String str = new CmsUtf8String("设备名称");
        assertEquals("UTF8String: 设备名称", str.toString());
    }

    @Test
    @DisplayName("static read method with fixed size and bmp mode")
    void staticReadFixedSizeWithBmpMode() throws Exception {
        CmsUtf8String str = new CmsUtf8String("test");
        str.size(10);
        str.bmp(true);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = CmsUtf8String.read(new PerInputStream(pos.toByteArray()), 
            AbstractCmsString.Mode.FIXED, 10, true);
        assertEquals("test", decoded.get());
        assertTrue(decoded.isBmp());
    }

    @Test
    @DisplayName("static read method with variable size")
    void staticReadVariableSize() throws Exception {
        CmsUtf8String str = new CmsUtf8String("test");
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = CmsUtf8String.read(new PerInputStream(pos.toByteArray()), 
            AbstractCmsString.Mode.VARIABLE, 100);
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsUtf8String original = new CmsUtf8String("test").max(100).bmp(true);
        CmsUtf8String cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertEquals(original.getMax(), cloned.getMax());
        assertTrue(cloned.isBmp());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy without bmp")
    void copyWithoutBmp() {
        CmsUtf8String original = new CmsUtf8String("test").max(100);
        CmsUtf8String cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertFalse(cloned.isBmp());
    }

    @Test
    @DisplayName("static read method with bmp mode")
    void staticReadMethodWithBmpMode() throws Exception {
        CmsUtf8String str = new CmsUtf8String("test");
        str.max(100);
        str.bmp(true);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsUtf8String decoded = new CmsUtf8String().max(100).bmp(true).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
        assertTrue(decoded.isBmp());
    }

    @Test
    @DisplayName("fixed size without bmp mode throws exception")
    void fixedSizeWithoutBmpModeThrowsException() {
        CmsUtf8String str = new CmsUtf8String("test");
        str.size(10);
        // bmp is false by default

        PerOutputStream pos = new PerOutputStream();
        assertThrows(UnsupportedOperationException.class, () -> str.encode(pos));
    }

    @Test
    @DisplayName("static write fixed size without bmp mode throws exception")
    void staticWriteFixedSizeWithoutBmpModeThrowsException() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(UnsupportedOperationException.class, 
            () -> CmsUtf8String.write(pos, "test", AbstractCmsString.Mode.FIXED, 10, false));
    }
}