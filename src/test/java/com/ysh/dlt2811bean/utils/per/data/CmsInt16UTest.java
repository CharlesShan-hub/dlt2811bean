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
        assertEquals(50000, CmsInt16U.decode(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, CmsInt16U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16U.MIN, CmsInt16U.decode(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, CmsInt16U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16U.MAX, CmsInt16U.decode(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt16U.decode(pis).get());
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
        CmsInt16U.validateValue(0);
        CmsInt16U.validateValue(32768);
        CmsInt16U.validateValue(65535);

        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.validateValue(-1));
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.validateValue(65536));
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.validateValue(null));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws PerDecodeException {
        CmsInt16U val = new CmsInt16U(10000);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt16U val2 = CmsInt16U.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(10000, val2.get());
        assertEquals(val.get(), val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt16U val = new CmsInt16U(10000);
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(10000, val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws PerDecodeException {
        CmsInt16U val = new CmsInt16U().set(50000);
        assertEquals(50000, val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt16U decoded = CmsInt16U.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(50000, decoded.get());
    }

    @Test
    @DisplayName("static encode with CmsInt16U object")
    void staticEncodeObject() throws PerDecodeException {
        CmsInt16U val = new CmsInt16U(50);

        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, val);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50, CmsInt16U.decode(pis).get());
    }

    @Test
    @DisplayName("static encode with null object")
    void staticEncodeNullObject() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.encode(pos, (CmsInt16U) null);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, CmsInt16U.decode(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt16U val = new CmsInt16U(50000);
        assertEquals("50000", val.toString());

        CmsInt16U val2 = new CmsInt16U(0);
        assertEquals("0", val2.toString());
    }
}