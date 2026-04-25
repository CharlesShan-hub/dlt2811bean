package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.numeric.CmsInt32U;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.write(pos, 3000000000L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(3000000000L, (long) CmsInt32U.read(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.write(pos, CmsInt32U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32U.MIN, (long) CmsInt32U.read(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.write(pos, CmsInt32U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt32U.MAX, (long) CmsInt32U.read(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt32U.write(pos, 0L);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0L, (long) CmsInt32U.read(pis).get());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.write(pos, -1L));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt32U.write(pos, 4294967296L));
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertDoesNotThrow(() -> new CmsInt32U(0L));
        assertDoesNotThrow(() -> new CmsInt32U(2147483648L));
        assertDoesNotThrow(() -> new CmsInt32U(4294967295L));

        assertThrows(IllegalArgumentException.class, () -> new CmsInt32U(-1L));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt32U(4294967296L));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws Exception {
        CmsInt32U val = new CmsInt32U(1000000000L);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt32U val2 = CmsInt32U.read(new PerInputStream(pos.toByteArray()));

        assertEquals(1000000000L, (long) val2.get());
        assertEquals((long) val.get(), (long) val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt32U val = new CmsInt32U(1000000000L);
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(1000000000L, (long) val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt32U val = new CmsInt32U().set(3000000000L);
        assertEquals(3000000000L, (long) val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt32U decoded = CmsInt32U.read(new PerInputStream(pos.toByteArray()));
        assertEquals(3000000000L, (long) decoded.get());
    }

    @Test
    @DisplayName("static encode with CmsInt32U object")
    void staticEncodeObject() throws Exception {
        CmsInt32U val = new CmsInt32U(500000000L);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(500000000L, (long) CmsInt32U.read(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt32U val = new CmsInt32U(3000000000L);
        assertEquals("INT32U: 3000000000", val.toString());

        CmsInt32U val2 = new CmsInt32U(0L);
        assertEquals("INT32U: 0", val2.toString());
    }
}