package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64U")
class CmsInt64UTest {

    private CmsInt64U getCmsInt64U() {
        return (CmsInt64U)(new CmsInt64U().test());
    }

    @Test
    void roundtrip() {
        CmsInt64U a = getCmsInt64U().value(1234567890123L);
        CmsInt64U b = getCmsInt64U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt64U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt64U src = getCmsInt64U().value(999L);
        CmsInt64U target = getCmsInt64U().decode(src.encode());
        assertEquals(src, target);
    }
}
