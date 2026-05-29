package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32")
class CmsInt32Test {

    @Test
    void positive() {
        byte[] data = new CmsInt32(2000000).encode();
        CmsInt32 r = CmsInt32.decode(data);
        assertEquals(2000000, (int) r.get());
    }

    @Test
    void negative() {
        byte[] data = new CmsInt32(-2000000).encode();
        CmsInt32 r = CmsInt32.decode(data);
        assertEquals(-2000000, (int) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt32(0).encode();
        CmsInt32 r = CmsInt32.decode(data);
        assertEquals(0, (int) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsInt32().get());
    }

    @Test
    void copy() {
        CmsInt32 original = new CmsInt32(-2000000);
        CmsInt32 cloned = original.copy();
        assertEquals(original.get(), cloned.get());
        assertNotSame(original, cloned);
    }

    @Test
    void roundtrip() {
        CmsInt32 original = new CmsInt32(-2000000);
        byte[] data = original.encode();
        CmsInt32 decoded = CmsInt32.decode(data);
        assertEquals(original.get(), decoded.get());
    }
}
