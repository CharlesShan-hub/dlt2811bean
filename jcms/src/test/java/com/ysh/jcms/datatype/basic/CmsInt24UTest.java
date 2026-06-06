package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt24U")
class CmsInt24UTest {

    @Test
    void roundtrip() {
        CmsInt24U original = new CmsInt24U().value(100000);
        assertEquals(original, new CmsInt24U().decode(original.encode()));
    }

    @Test
    void maxValue() {
        int max = (1 << 24) - 1;
        assertEquals(new CmsInt24U().value(max),
                     new CmsInt24U().decode(new CmsInt24U().value(max).encode()));
    }

    @Test
    void zero() {
        assertEquals(new CmsInt24U().value(0),
                     new CmsInt24U().decode(new CmsInt24U().value(0).encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt24U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt24U target = new CmsInt24U().value(999999);
        target.decode(new CmsInt24U().value(42).encode());
        assertEquals(new CmsInt24U().value(42), target);
    }
}
