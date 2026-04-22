package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8")
class CmsInt8Test {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt8(42).encode(pos);

        CmsInt8 r = new CmsInt8().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(42, r.get());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt8(-42).encode(pos);

        CmsInt8 r = new CmsInt8().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(-42, r.get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt8(CmsInt8.MIN).encode(pos);

        CmsInt8 r = new CmsInt8().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt8.MIN, r.get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt8(CmsInt8.MAX).encode(pos);

        CmsInt8 r = new CmsInt8().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt8.MAX, r.get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt8(0).encode(pos);

        CmsInt8 r = new CmsInt8().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0, r.get());
    }

    @Test
    @DisplayName("default value is 0")
    void defaultValue() {
        assertEquals(0, new CmsInt8().get());
    }

    @Test
    @DisplayName("value below minimum throws exception")
    void belowMin() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8(-129));
    }

    @Test
    @DisplayName("value above maximum throws exception")
    void aboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8(128));
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsInt8 val = new CmsInt8();
        val.set(42);
        assertEquals(42, val.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt8().set(null));
    }

    @Test
    @DisplayName("set out of range throws exception")
    void setOutOfRange() {
        CmsInt8 val = new CmsInt8();
        assertThrows(IllegalArgumentException.class, () -> val.set(128));
        assertThrows(IllegalArgumentException.class, () -> val.set(-129));
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt8 val = new CmsInt8().set(42);
        assertEquals(42, val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt8 decoded = new CmsInt8().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(42, decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        assertEquals("INT8: 42", new CmsInt8(42).toString());
        assertEquals("INT8: 0", new CmsInt8(0).toString());
    }
}
