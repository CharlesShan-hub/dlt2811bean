package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    @Test
    void positive() {
        byte[] data = new CmsInt64(9000000000000L).encode();
        CmsInt64 r = CmsInt64.from(data);
        assertEquals(9000000000000L, (long) r.get());
    }

    @Test
    void negative() {
        byte[] data = new CmsInt64(-9000000000000L).encode();
        CmsInt64 r = CmsInt64.from(data);
        assertEquals(-9000000000000L, (long) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt64(0L).encode();
        CmsInt64 r = CmsInt64.from(data);
        assertEquals(0L, (long) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0L, (long) new CmsInt64().get());
    }

    @Test
    void copy() {
        CmsInt64 original = new CmsInt64(-9000000000000L);
        CmsInt64 cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }

    @Test
    void roundtrip() {
        CmsInt64 original = new CmsInt64(-9000000000000L);
        byte[] data = original.encode();
        CmsInt64 decoded = CmsInt64.from(data);
        assertEquals(original.get(), decoded.get());
    }
}
