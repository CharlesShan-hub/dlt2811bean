package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt24U")
class CmsInt24UTest {

    @Test
    void positive() {
        byte[] data = new CmsInt24U(1000000).encode();
        CmsInt24U r = CmsInt24U.from(data);
        assertEquals(1000000, (int) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt24U(0).encode();
        CmsInt24U r = CmsInt24U.from(data);
        assertEquals(0, (int) r.get());
    }

    @Test
    void roundtrip() {
        CmsInt24U original = new CmsInt24U(16777215);
        byte[] data = original.encode();
        CmsInt24U decoded = CmsInt24U.from(data);
        assertEquals(original.get(), decoded.get());
    }
}
