package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.numeric.CmsInt8U;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8U")
class CmsInt8UTest {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.write(pos, 200);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(200, (int) CmsInt8U.read(pis).get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.write(pos, CmsInt8U.MIN);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8U.MIN, (int) CmsInt8U.read(pis).get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.write(pos, CmsInt8U.MAX);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(CmsInt8U.MAX, (int) CmsInt8U.read(pis).get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        CmsInt8U.write(pos, 0);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(0, (int) CmsInt8U.read(pis).get());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.write(pos, -1));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        PerOutputStream pos = new PerOutputStream();
        assertThrows(IllegalArgumentException.class, () -> CmsInt8U.write(pos, 256));
    }

    @Test
    @DisplayName("constructor validates range")
    void constructorValidatesRange() {
        assertDoesNotThrow(() -> new CmsInt8U(0));
        assertDoesNotThrow(() -> new CmsInt8U(128));
        assertDoesNotThrow(() -> new CmsInt8U(255));

        assertThrows(IllegalArgumentException.class, () -> new CmsInt8U(-1));
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8U(256));
    }

    @Test
    @DisplayName("instance encode+decode")
    void instanceRoundTrip() throws Exception {
        CmsInt8U val = new CmsInt8U(100);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt8U val2 = CmsInt8U.read(new PerInputStream(pos.toByteArray()));

        assertEquals(100, (int) val2.get());
        assertEquals((int) val.get(), (int) val2.get());
    }

    @Test
    @DisplayName("set method with null value throws exception")
    void setNullValue() {
        CmsInt8U val = new CmsInt8U(100);
        assertThrows(IllegalArgumentException.class, () -> val.set(null));
        assertEquals(100, (int) val.get());
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt8U val = new CmsInt8U().set(200);
        assertEquals(200, (int) val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt8U decoded = CmsInt8U.read(new PerInputStream(pos.toByteArray()));
        assertEquals(200, (int) decoded.get());
    }

    @Test
    @DisplayName("static write with CmsInt8U object")
    void staticWriteObject() throws Exception {
        CmsInt8U val = new CmsInt8U(50);

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        PerInputStream pis = new PerInputStream(pos.toByteArray());
        assertEquals(50, (int) CmsInt8U.read(pis).get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        CmsInt8U val = new CmsInt8U(200);
        assertEquals("INT8U: 200", val.toString());

        CmsInt8U val2 = new CmsInt8U(0);
        assertEquals("INT8U: 0", val2.toString());
    }
}