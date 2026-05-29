package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16")
class CmsInt16Test {

    @Test
    void positive() {
        byte[] data = new CmsInt16(12345).encode();
        CmsInt16 r = CmsInt16.decode(data);
        assertEquals(12345, (int) r.get());
    }

    @Test
    void negative() {
        byte[] data = new CmsInt16(-12345).encode();
        CmsInt16 r = CmsInt16.decode(data);
        assertEquals(-12345, (int) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt16(0).encode();
        CmsInt16 r = CmsInt16.decode(data);
        assertEquals(0, (int) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsInt16().get());
    }

    @Test
    void copy() {
        CmsInt16 original = new CmsInt16(-12345);
        CmsInt16 cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    void roundtrip() {
        CmsInt16 original = new CmsInt16(-12345);
        byte[] data = original.encode();
        CmsInt16 decoded = CmsInt16.decode(data);
        assertEquals(original.get(), decoded.get());
    }
}
