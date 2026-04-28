package com.ysh.dlt2811bean.datatypes.numeric;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16")
class CmsInt16Test {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt16(1000).encode(pos);

        CmsInt16 r = new CmsInt16().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1000, (int) r.get());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt16(-1000).encode(pos);

        CmsInt16 r = new CmsInt16().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(-1000, (int) r.get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt16(CmsInt16.MIN).encode(pos);

        CmsInt16 r = new CmsInt16().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt16.MIN, r.get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt16(CmsInt16.MAX).encode(pos);

        CmsInt16 r = new CmsInt16().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt16.MAX, r.get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt16(0).encode(pos);

        CmsInt16 r = new CmsInt16().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0, (int) r.get());
    }

    @Test
    @DisplayName("default value is 0")
    void defaultValue() {
        assertEquals(0, new CmsInt16().get());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16(-32769));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16(32768));
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsInt16 val = new CmsInt16();
        val.set(1000);
        assertEquals(1000, (int) val.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt16().set(null));
    }

    @Test
    @DisplayName("set out of range throws exception")
    void setOutOfRange() {
        CmsInt16 val = new CmsInt16();
        assertThrows(IllegalArgumentException.class, () -> val.set(32768));
        assertThrows(IllegalArgumentException.class, () -> val.set(-32769));
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt16 val = new CmsInt16().set(1000);
        assertEquals(1000, (int) val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt16 decoded = new CmsInt16().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1000, (int) decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        assertEquals("(CmsInt16) 1000", new CmsInt16(1000).toString());
        assertEquals("(CmsInt16) 0", new CmsInt16(0).toString());
    }
}
