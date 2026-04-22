package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    @Test
    @DisplayName("positive value")
    void positive() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64.encode(pos, 1234567890L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1234567890L, CmsInt64.decode(pis).getValue());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64.encode(pos, -1234567890L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(-1234567890L, CmsInt64.decode(pis).getValue());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64.encode(pos, CmsInt64.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt64.MIN, CmsInt64.decode(pis).getValue());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64.encode(pos, CmsInt64.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt64.MAX, CmsInt64.decode(pis).getValue());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64.encode(pos, 0L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0L, CmsInt64.decode(pis).getValue());
    }

    @Test
    @DisplayName("validateValue method always returns true")
    void validateValue() {
        assertTrue(CmsInt64.validateValue(0L));
        assertTrue(CmsInt64.validateValue(CmsInt64.MIN));
        assertTrue(CmsInt64.validateValue(CmsInt64.MAX));
        assertTrue(CmsInt64.validateValue(-1L));
        assertTrue(CmsInt64.validateValue(1L));
    }

    @Test
    @DisplayName("bean encode overload")
    void beanEncode() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt64.encode(pos, new CmsInt64(1234567890L));

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(1234567890L, CmsInt64.decode(pis).getValue());
    }

    @Test
    @DisplayName("bean chain setter")
    void chainSetter() {
        CmsInt64 val = new CmsInt64().setValue(1234567890L);
        assertEquals(1234567890L, val.getValue());
    }

    @Test
    @DisplayName("default constructor")
    void defaultConstructor() {
        CmsInt64 val = new CmsInt64();
        assertEquals(0L, val.getValue());
    }

    @Test
    @DisplayName("constructor with value")
    void constructorWithValue() {
        CmsInt64 val = new CmsInt64(1234567890L);
        assertEquals(1234567890L, val.getValue());
    }

    @Test
    @DisplayName("constructor accepts any long value")
    void constructorAcceptsAnyValue() {
        assertDoesNotThrow(() -> new CmsInt64(CmsInt64.MIN));
        assertDoesNotThrow(() -> new CmsInt64(CmsInt64.MAX));
        assertDoesNotThrow(() -> new CmsInt64(0L));
        assertDoesNotThrow(() -> new CmsInt64(-1L));
        assertDoesNotThrow(() -> new CmsInt64(1L));
    }

    @Test
    @DisplayName("toString method")
    void toStringMethod() {
        CmsInt64 val = new CmsInt64(1234567890L);
        assertEquals("1234567890", val.toString());
    }
}