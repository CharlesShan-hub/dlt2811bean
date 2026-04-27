package com.ysh.dlt2811bean.datatypes.numeric;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    @DisplayName("true value")
    void trueValue() throws Exception {
        PerOutputStream pos = new PerOutputStream();

        CmsBoolean.write(pos, true);

        PerInputStream pis = new PerInputStream(pos.toByteArray());

        CmsBoolean r = CmsBoolean.read(pis);
        assertTrue(r.get());
    }

    @Test
    @DisplayName("false value")
    void falseValue() throws Exception {
        PerOutputStream pos = new PerOutputStream();
        new CmsBoolean(false).encode(pos);

        CmsBoolean r = new CmsBoolean().decode(new PerInputStream(pos.toByteArray()));
        assertFalse(r.get());
    }

    @Test
    @DisplayName("default value is false")
    void defaultValue() {
        assertFalse(new CmsBoolean().get());
    }

    @Test
    @DisplayName("set method")
    void set() {
        CmsBoolean val = new CmsBoolean();
        val.set(true);
        assertTrue(val.get());
        val.set(false);
        assertFalse(val.get());
    }

    @Test
    @DisplayName("set null throws exception")
    void setNull() {
        assertThrows(IllegalArgumentException.class, () -> new CmsBoolean().set(null));
    }

    @Test
    @DisplayName("chain usage")
    void chainUsage() throws Exception {
        CmsBoolean val = new CmsBoolean().set(true);
        assertTrue(val.get());

        PerOutputStream pos = new PerOutputStream();
        val.encode(pos);

        CmsBoolean decoded = new CmsBoolean().decode(new PerInputStream(pos.toByteArray()));
        assertTrue(decoded.get());
    }

    @Test
    @DisplayName("toString")
    void toStringTest() {
        assertEquals("BOOLEAN: true", new CmsBoolean(true).toString());
        assertEquals("BOOLEAN: false", new CmsBoolean(false).toString());
    }
}
