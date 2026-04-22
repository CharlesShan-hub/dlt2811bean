package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, 3000000000L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(3000000000L, CmsInt32U.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, CmsInt32U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32U.MIN, CmsInt32U.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, CmsInt32U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32U.MAX, CmsInt32U.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, 0L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0L, CmsInt32U.decode(pis).getValue());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.encode(pos, -1L));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.encode(pos, 4294967296L));
    }

    @Test
    @DisplayName("validateValue method")
    void validateValue() {
        assertDoesNotThrow(() -> CmsInt32U.validateValue(0L));
        assertDoesNotThrow(() -> CmsInt32U.validateValue(CmsInt32U.MIN));
        assertDoesNotThrow(() -> CmsInt32U.validateValue(CmsInt32U.MAX));
        
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.validateValue(-1L));
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.validateValue(4294967296L));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, new CmsInt32U(3000000000L));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(3000000000L, CmsInt32U.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt32U val = new CmsInt32U().setValue(3000000000L);
        assertEquals(3000000000L, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt32U val = new CmsInt32U();
        assertEquals(0L, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt32U val = new CmsInt32U(3000000000L);
        assertEquals(3000000000L, val.getValue());
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt32U(-1L));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt32U(4294967296L));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt32U val = new CmsInt32U(3000000000L);
        assertEquals("3000000000", val.toString());
    }
}