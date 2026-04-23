package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    @Test
    @DisplayName("positive value")
    void positive() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt64(1000000000000L).encode(pos);

        CmsInt64 r = CmsInt64.read(new PerInputStream(pos.toByteArray()));
        assertEquals(1000000000000L, (long) r.get());
    }

    @Test
    @DisplayName("negative value")
    void negative() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt64(-1000000000000L).encode(pos);

        CmsInt64 r = new CmsInt64().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(-1000000000000L, (long) r.get());
    }

    @Test
    @DisplayName("minimum value")
    void min() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt64(CmsInt64.MIN).encode(pos);

        CmsInt64 r = new CmsInt64().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt64.MIN, r.get());
    }

    @Test
    @DisplayName("maximum value")
    void max() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt64(CmsInt64.MAX).encode(pos);

        CmsInt64 r = new CmsInt64().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsInt64.MAX, r.get());
    }

    @Test
    @DisplayName("zero")
    void zero() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsInt64(0L).encode(pos);

        CmsInt64 r = new CmsInt64().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(0L, (long) r.get());
    }

    @Test
    @DisplayName("default value is 0")
    void defaultValue() {
        assertEquals(0L, new CmsInt64().get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsInt64 val = new CmsInt64();
        val.set(1000000000000L);
        assertEquals(1000000000000L, (long) val.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsInt64().set(null));
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsInt64 val = new CmsInt64().set(1000000000000L);
        assertEquals(1000000000000L, (long) val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsInt64 decoded = new CmsInt64().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(1000000000000L, (long) decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        assertEquals("INT64: 1000000000000", new CmsInt64(1000000000000L).toString());
        assertEquals("INT64: 0", new CmsInt64(0L).toString());
    }
}