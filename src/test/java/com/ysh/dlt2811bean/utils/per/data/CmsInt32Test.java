package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32")
class CmsInt32Test {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32.encode(pos, 100000);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(100000, CmsInt32.decode(pis).getValue());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32.encode(pos, -100000);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-100000, CmsInt32.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32.encode(pos, CmsInt32.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32.MIN, CmsInt32.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32.encode(pos, CmsInt32.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32.MAX, CmsInt32.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt32.decode(pis).getValue());
    }

    @Test
    @DisplayName("validateValue method always returns true")
    void validateValue() {
        assertTrue(CmsInt32.validateValue(0));
        assertTrue(CmsInt32.validateValue(CmsInt32.MIN));
        assertTrue(CmsInt32.validateValue(CmsInt32.MAX));
        assertTrue(CmsInt32.validateValue(-1));
        assertTrue(CmsInt32.validateValue(1));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32.encode(pos, new CmsInt32(100000));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(100000, CmsInt32.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt32 val = new CmsInt32().setValue(100000);
        assertEquals(100000, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt32 val = new CmsInt32();
        assertEquals(0, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt32 val = new CmsInt32(100000);
        assertEquals(100000, val.getValue());
    }

    @Test
    @DisplayName("constructor accepts any int value")
    void constructorAcceptsAnyValue() {
        assertDoesNotThrow(() -> new CmsInt32(CmsInt32.MIN));
        assertDoesNotThrow(() -> new CmsInt32(CmsInt32.MAX));
        assertDoesNotThrow(() -> new CmsInt32(0));
        assertDoesNotThrow(() -> new CmsInt32(-1));
        assertDoesNotThrow(() -> new CmsInt32(1));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt32 val = new CmsInt32(100000);
        assertEquals("100000", val.toString());
    }
}