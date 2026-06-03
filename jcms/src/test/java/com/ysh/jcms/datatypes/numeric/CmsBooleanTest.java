package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsBoolean")
class CmsBooleanTest {

    @Test
    void trueValue() {
        byte[] data = CmsBoolean.TRUE.encode();
        CmsBoolean r = CmsBoolean.from(data);
        assertTrue(r.get());
    }

    @Test
    void falseValue() {
        byte[] data = CmsBoolean.FALSE.encode();
        CmsBoolean r = CmsBoolean.from(data);
        assertFalse(r.get());
    }

    @Test
    void constructTrue() {
        CmsBoolean val = new CmsBoolean(true);
        assertTrue(val.get());
    }

    @Test
    void constructFalse() {
        CmsBoolean val = new CmsBoolean(false);
        assertFalse(val.get());
    }

    @Test
    void defaultValue() {
        assertFalse(new CmsBoolean().get());
    }

    @Test
    void set() {
        CmsBoolean val = new CmsBoolean();
        val.set(true);
        assertTrue(val.get());
        val.set(false);
        assertFalse(val.get());
    }

    @Test
    void copy() {
        CmsBoolean original = CmsBoolean.TRUE;
        CmsBoolean cloned = original.copy();
        assertTrue(cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    void roundtrip() {
        CmsBoolean original = new CmsBoolean(true);
        byte[] data = original.encode();
        CmsBoolean decoded = CmsBoolean.from(data);
        assertEquals(original.get(), decoded.get());
    }
}
