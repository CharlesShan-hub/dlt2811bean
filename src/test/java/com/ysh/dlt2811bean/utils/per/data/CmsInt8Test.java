package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8")
class CmsInt8Test {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8.encode(pos, 42);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(42, CmsInt8.decode(pis).getValue());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8.encode(pos, -42);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-42, CmsInt8.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8.encode(pos, CmsInt8.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8.MIN, CmsInt8.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8.encode(pos, CmsInt8.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8.MAX, CmsInt8.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt8.decode(pis).getValue());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt8.encode(pos, -129));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt8.encode(pos, 128));
    }

    @Test
    @DisplayName("validateValue method")
    void validateValue() {
        assertDoesNotThrow(() -> CmsInt8.validateValue(0));
        assertDoesNotThrow(() -> CmsInt8.validateValue(CmsInt8.MIN));
        assertDoesNotThrow(() -> CmsInt8.validateValue(CmsInt8.MAX));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt8.validateValue(-129));
        assertThrows(IllegalArgumentException.class, () -> CmsInt8.validateValue(128));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8.encode(pos, new CmsInt8(42));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(42, CmsInt8.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt8 val = new CmsInt8().setValue(42);
        assertEquals(42, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt8 val = new CmsInt8();
        assertEquals(0, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt8 val = new CmsInt8(42);
        assertEquals(42, val.getValue());
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8(-129));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8(128));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt8 val = new CmsInt8(42);
        assertEquals("42", val.toString());
    }
}