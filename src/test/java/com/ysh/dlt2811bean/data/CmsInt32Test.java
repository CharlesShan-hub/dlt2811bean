package com.ysh.dlt2811bean.data;

import com.ysh.dlt2811bean.data.numeric.CmsInt32;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32")
class CmsInt32Test {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt32(1000000).encode(pos);

        CmsInt32 r = new CmsInt32().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1000000, (int) r.get());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt32(-1000000).encode(pos);

        CmsInt32 r = new CmsInt32().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(-1000000, (int) r.get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt32(CmsInt32.MIN).encode(pos);

        CmsInt32 r = new CmsInt32().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt32.MIN, r.get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt32(CmsInt32.MAX).encode(pos);

        CmsInt32 r = new CmsInt32().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt32.MAX, r.get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt32(0).encode(pos);

        CmsInt32 r = new CmsInt32().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0, (int) r.get());
    }

    @Test
    @DisplayName("default value is 0")
    void defaultValue() {
        assertEquals(0, new CmsInt32().get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsInt32 val = new CmsInt32();
        val.set(1000000);
        assertEquals(1000000, (int) val.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt32().set(null));
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt32 val = new CmsInt32().set(1000000);
        assertEquals(1000000, (int) val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt32 decoded = new CmsInt32().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1000000, (int) decoded.get());
    }

    @Test
    @DisplayName("copy")
    void copy() {
        CmsInt32 original = new CmsInt32(1000000);
        CmsInt32 cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    @DisplayName("copy is independent")
    void copyIsIndependent() {
        CmsInt32 original = new CmsInt32(1000000);
        CmsInt32 cloned = original.copy();
        cloned.set(2000000);
        assertEquals(1000000, (int) original.get());
        assertEquals(2000000, (int) cloned.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        assertEquals("INT32: 1000000", new CmsInt32(1000000).toString());
        assertEquals("INT32: 0", new CmsInt32(0).toString());
    }
}
