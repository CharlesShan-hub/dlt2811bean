package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8U")
class CmsInt8UTest {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, 200);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(200, CmsInt8U.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, CmsInt8U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8U.MIN, CmsInt8U.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, CmsInt8U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8U.MAX, CmsInt8U.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt8U.decode(pis).getValue());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.encode(pos, -1));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.encode(pos, 256));
    }

    @Test
    @DisplayName("validateValue method")
    void validateValue() {
        assertDoesNotThrow(() -> CmsInt8U.validateValue(0));
        assertDoesNotThrow(() -> CmsInt8U.validateValue(CmsInt8U.MIN));
        assertDoesNotThrow(() -> CmsInt8U.validateValue(CmsInt8U.MAX));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.validateValue(-1));
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.validateValue(256));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, new CmsInt8U(200));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(200, CmsInt8U.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt8U val = new CmsInt8U().setValue(200);
        assertEquals(200, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt8U val = new CmsInt8U();
        assertEquals(0, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt8U val = new CmsInt8U(200);
        assertEquals(200, val.getValue());
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8U(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8U(256));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt8U val = new CmsInt8U(200);
        assertEquals("200", val.toString());
    }
}