package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsVisibleString")
class CmsVisibleStringTest {

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsVisibleString str = new CmsVisibleString("test");
        assertEquals("test", str.get());
    }

    @Test
    @DisplayName("constructor with null value")
    void constructorWithNullValue() {
        CmsVisibleString str = new CmsVisibleString(null);
        assertEquals("", str.get());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsVisibleString str = new CmsVisibleString();
        assertEquals("", str.get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsVisibleString str = new CmsVisibleString();
        str.set("hello");
        assertEquals("hello", str.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsVisibleString().set(null));
    }

    @Test
    @DisplayName("size method")
    void size() {
        CmsVisibleString str = new CmsVisibleString("test");
        str.size(10);
        assertEquals(10, str.size);
    }

    @Test
    @DisplayName("max method")
    void max() {
        CmsVisibleString str = new CmsVisibleString("test");
        str.max(100);
        assertEquals(100, str.max);
    }

    @Test
    @DisplayName("size clears max")
    void sizeClearsMax() {
        CmsVisibleString str = new CmsVisibleString("test");
        str.max(100);
        str.size(10);
        assertEquals(10, str.size);
        assertNull(str.max);
    }

    @Test
    @DisplayName("max clears size")
    void maxClearsSize() {
        CmsVisibleString str = new CmsVisibleString("test");
        str.size(10);
        str.max(100);
        assertEquals(100, str.max);
        assertNull(str.size);
    }

    @Test
    @DisplayName("encode/decode fixed size")
    void encodeDecodeFixedSize() throws Exception {
        CmsVisibleString str = new CmsVisibleString("test");
        str.size(10);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = new CmsVisibleString().size(10).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("encode/decode variable size")
    void encodeDecodeVariableSize() throws Exception {
        CmsVisibleString str = new CmsVisibleString("test");
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = new CmsVisibleString().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("encode/decode empty string")
    void encodeDecodeEmptyString() throws Exception {
        CmsVisibleString str = new CmsVisibleString("");
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = new CmsVisibleString().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("", decoded.get());
    }

    @Test
    @DisplayName("encode/decode long string")
    void encodeDecodeLongString() throws Exception {
        String longString = "This is a longer visible string for testing purposes";
        CmsVisibleString str = new CmsVisibleString(longString);
        str.max(255);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = new CmsVisibleString().max(255).decode(new PerInputStream(pos.toByteArray()));
        assertEquals(longString, decoded.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsVisibleString str = new CmsVisibleString()
            .set("test")
            .max(100);

        assertEquals("test", str.get());
        assertEquals(100, str.max);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = new CmsVisibleString().max(100).decode(new PerInputStream(pos.toByteArray()));
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsVisibleString str = new CmsVisibleString("test");
        assertEquals("VISIBLE STRING: test", str.toString());
    }

    @Test
    @DisplayName("static read method with fixed size")
    void staticReadFixedSize() throws Exception {
        CmsVisibleString str = new CmsVisibleString("test");
        str.size(10);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = CmsVisibleString.read(new PerInputStream(pos.toByteArray()), 
            AbstractCmsString.Mode.FIXED, 10);
        assertEquals("test", decoded.get());
    }

    @Test
    @DisplayName("static read method with variable size")
    void staticReadVariableSize() throws Exception {
        CmsVisibleString str = new CmsVisibleString("test");
        str.max(100);

        PerOutputStream pos = new PerOutputStream();
        str.encode(pos);

        CmsVisibleString decoded = CmsVisibleString.read(new PerInputStream(pos.toByteArray()), 
            AbstractCmsString.Mode.VARIABLE, 100);
        assertEquals("test", decoded.get());
    }
}