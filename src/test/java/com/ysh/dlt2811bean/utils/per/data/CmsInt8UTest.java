package com.ysh.dlt2811bean.utils.per.data;

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
        assertEquals(200, CmsInt8U.decode(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, CmsInt8U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8U.MIN, CmsInt8U.decode(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, CmsInt8U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8U.MAX, CmsInt8U.decode(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt8U.decode(pis).get());
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
        CmsInt8U.validateValue(0);
        CmsInt8U.validateValue(128);
        CmsInt8U.validateValue(255);

        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.validateValue(-1));
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.validateValue(256));
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.validateValue(null));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws PerDecodeException {
        CmsInt8U val = new CmsInt8U(100);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt8U val2 = CmsInt8U.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(100, val2.get());
        assertEquals(val.get(), val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt8U val = new CmsInt8U(100);
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(100, val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws PerDecodeException {
        CmsInt8U val = new CmsInt8U().set(200);
        assertEquals(200, val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt8U decoded = CmsInt8U.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(200, decoded.get());
    }

    @Test
    @DisplayName("static encode with CmsInt8U object")
    void staticEncodeObject() throws PerDecodeException {
        CmsInt8U val = new CmsInt8U(50);

        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, val);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50, CmsInt8U.decode(pis).get());
    }

    @Test
    @DisplayName("static encode with null object")
    void staticEncodeNullObject() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.encode(pos, (CmsInt8U) null);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt8U.decode(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt8U val = new CmsInt8U(200);
        assertEquals("200", val.toString());

        CmsInt8U val2 = new CmsInt8U(0);
        assertEquals("0", val2.toString());
    }
}