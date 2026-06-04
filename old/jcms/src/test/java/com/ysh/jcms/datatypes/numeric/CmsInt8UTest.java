package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8U")
class CmsInt8UTest {

    @Test
    void positive() {
        byte[] data = new CmsInt8U(200).encode();
        CmsInt8U r = CmsInt8U.from(data);
        assertEquals(200, (int) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt8U(0).encode();
        CmsInt8U r = CmsInt8U.from(data);
        assertEquals(0, (int) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsInt8U().get());
    }

    @Test
    void copy() {
        CmsInt8U original = new CmsInt8U(200);
        CmsInt8U cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }

    @Test
    void roundtrip() {
        CmsInt8U original = new CmsInt8U(200);
        byte[] data = original.encode();
        CmsInt8U decoded = CmsInt8U.from(data);
        assertEquals(original.get(), decoded.get());
    }
}
