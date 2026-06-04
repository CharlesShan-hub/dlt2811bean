package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    @Test
    void positive() {
        byte[] data = new CmsInt32U(3000000000L).encode();
        CmsInt32U r = CmsInt32U.from(data);
        assertEquals(3000000000L, (long) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt32U(0L).encode();
        CmsInt32U r = CmsInt32U.from(data);
        assertEquals(0L, (long) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0L, (long) new CmsInt32U().get());
    }

    @Test
    void copy() {
        CmsInt32U original = new CmsInt32U(3000000000L);
        CmsInt32U cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }

    @Test
    void roundtrip() {
        CmsInt32U original = new CmsInt32U(3000000000L);
        byte[] data = original.encode();
        CmsInt32U decoded = CmsInt32U.from(data);
        assertEquals(original.get(), decoded.get());
    }
}
