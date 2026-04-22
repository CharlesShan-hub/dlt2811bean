package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16U")
class CmsInt16UTest {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, 50000);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50000, CmsInt16U.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, CmsInt16U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16U.MIN, CmsInt16U.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, CmsInt16U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16U.MAX, CmsInt16U.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt16U.decode(pis).getValue());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.encode(pos, -1));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.encode(pos, 65536));
    }

    @Test
    @DisplayName("validateValue method")
    void validateValue() {
        assertDoesNotThrow(() -> CmsInt16U.validateValue(0));
        assertDoesNotThrow(() -> CmsInt16U.validateValue(CmsInt16U.MIN));
        assertDoesNotThrow(() -> CmsInt16U.validateValue(CmsInt16U.MAX));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.validateValue(-1));
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.validateValue(65536));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, new CmsInt16U(50000));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50000, CmsInt16U.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt16U val = new CmsInt16U().setValue(50000);
        assertEquals(50000, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt16U val = new CmsInt16U();
        assertEquals(0, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt16U val = new CmsInt16U(50000);
        assertEquals(50000, val.getValue());
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16U(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16U(65536));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt16U val = new CmsInt16U(50000);
        assertEquals("50000", val.toString());
    }
}