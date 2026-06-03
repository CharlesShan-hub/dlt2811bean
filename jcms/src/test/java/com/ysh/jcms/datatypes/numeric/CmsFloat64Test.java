package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat64")
class CmsFloat64Test {

    @Test
    void positive() {
        byte[] data = new CmsFloat64(2.718281828459045).encode();
        CmsFloat64 r = CmsFloat64.from(data);
        assertEquals(2.718281828459045, r.get(), 1e-15);
    }

    @Test
    void zero() {
        byte[] data = new CmsFloat64(0.0).encode();
        CmsFloat64 r = CmsFloat64.from(data);
        assertEquals(0.0, r.get(), 1e-15);
    }

    @Test
    void defaultValue() {
        assertEquals(0.0, new CmsFloat64().get(), 1e-15);
    }

    @Test
    void roundtrip() {
        CmsFloat64 original = new CmsFloat64(2.718281828459045);
        byte[] data = original.encode();
        CmsFloat64 decoded = CmsFloat64.from(data);
        assertEquals(original.get(), decoded.get(), 1e-15);
    }
}
