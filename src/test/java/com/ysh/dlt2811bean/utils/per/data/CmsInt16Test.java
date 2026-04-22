package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16")
class CmsInt16Test {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16.encode(pos, 1000);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1000, CmsInt16.decode(pis).getValue());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16.encode(pos, -1000);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-1000, CmsInt16.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16.encode(pos, CmsInt16.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16.MIN, CmsInt16.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16.encode(pos, CmsInt16.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16.MAX, CmsInt16.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt16.decode(pis).getValue());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt16.encode(pos, -32769));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt16.encode(pos, 32768));
    }

    @Test
    @DisplayName("validateValue method")
    void validateValue() {
        assertDoesNotThrow(() -> CmsInt16.validateValue(0));
        assertDoesNotThrow(() -> CmsInt16.validateValue(CmsInt16.MIN));
        assertDoesNotThrow(() -> CmsInt16.validateValue(CmsInt16.MAX));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt16.validateValue(-32769));
        assertThrows(IllegalArgumentException.class, () -> CmsInt16.validateValue(32768));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16.encode(pos, new CmsInt16(1000));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1000, CmsInt16.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt16 val = new CmsInt16().setValue(1000);
        assertEquals(1000, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt16 val = new CmsInt16();
        assertEquals(0, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt16 val = new CmsInt16(1000);
        assertEquals(1000, val.getValue());
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16(-32769));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16(32768));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt16 val = new CmsInt16(1000);
        assertEquals("1000", val.toString());
    }
}