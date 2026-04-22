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
        assertEquals(3000000000L, CmsInt32U.decode(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, CmsInt32U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32U.MIN, CmsInt32U.decode(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, CmsInt32U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32U.MAX, CmsInt32U.decode(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, 0L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0L, CmsInt32U.decode(pis).get());
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
        CmsInt32U.validateValue(0L);
        CmsInt32U.validateValue(2147483648L);
        CmsInt32U.validateValue(4294967295L);

        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.validateValue(-1L));
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.validateValue(4294967296L));
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.validateValue(null));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws PerDecodeException {
        CmsInt32U val = new CmsInt32U(1000000000L);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt32U val2 = CmsInt32U.decode(new PerInputStream(pos.toByteArray()));

        assertEquals(1000000000L, val2.get());
        assertEquals(val.get(), val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt32U val = new CmsInt32U(1000000000L);
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(1000000000L, val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws PerDecodeException {
        CmsInt32U val = new CmsInt32U().set(3000000000L);
        assertEquals(3000000000L, val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt32U decoded = CmsInt32U.decode(new PerInputStream(pos.toByteArray()));
        assertEquals(3000000000L, decoded.get());
    }

    @Test
    @DisplayName("static encode with CmsInt32U object")
    void staticEncodeObject() throws PerDecodeException {
        CmsInt32U val = new CmsInt32U(500000000L);

        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, val);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(500000000L, CmsInt32U.decode(pis).get());
    }

    @Test
    @DisplayName("static encode with null object")
    void staticEncodeNullObject() throws PerDecodeException {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.encode(pos, (CmsInt32U) null);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0L, CmsInt32U.decode(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt32U val = new CmsInt32U(3000000000L);
        assertEquals("3000000000", val.toString());

        CmsInt32U val2 = new CmsInt32U(0L);
        assertEquals("0", val2.toString());
    }
}