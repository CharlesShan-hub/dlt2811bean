package com.ysh.dlt2811bean.datatypes;

import com.ysh.dlt2811bean.datatypes.numeric.CmsInt16U;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16U")
class CmsInt16UTest {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.write(pos, 50000);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50000, (int) CmsInt16U.read(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.write(pos, CmsInt16U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16U.MIN, (int) CmsInt16U.read(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.write(pos, CmsInt16U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt16U.MAX, (int) CmsInt16U.read(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt16U.write(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, (int) CmsInt16U.read(pis).get());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.write(pos, -1));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt16U.write(pos, 65536));
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertDoesNotThrow(() -> new CmsInt16U(0));
        assertDoesNotThrow(() -> new CmsInt16U(32768));
        assertDoesNotThrow(() -> new CmsInt16U(65535));

        assertThrows(IllegalArgumentException.class, () -> new CmsInt16U(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16U(65536));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws Exception {
        CmsInt16U val = new CmsInt16U(10000);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt16U val2 = CmsInt16U.read(new PerInputStream(pos.toByteArray()));

        assertEquals(10000, (int) val2.get());
        assertEquals((int) val.get(), (int) val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt16U val = new CmsInt16U(10000);
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(10000, (int) val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt16U val = new CmsInt16U().set(50000);
        assertEquals(50000, (int) val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt16U decoded = CmsInt16U.read(new PerInputStream(pos.toByteArray()));
        assertEquals(50000, (int) decoded.get());
    }

    @Test
    @DisplayName("static write with CmsInt16U object")
    void staticWriteObject() throws Exception {
        CmsInt16U val = new CmsInt16U(50);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50, (int) CmsInt16U.read(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt16U val = new CmsInt16U(50000);
        assertEquals("INT16U: 50000", val.toString());

        CmsInt16U val2 = new CmsInt16U(0);
        assertEquals("INT16U: 0", val2.toString());
    }
}