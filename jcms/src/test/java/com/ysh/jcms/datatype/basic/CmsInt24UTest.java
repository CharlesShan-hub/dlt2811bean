package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt24U")
class CmsInt24UTest {

    private CmsInt24U getCmsInt24U() {
        return (CmsInt24U)(new CmsInt24U().test());
    }

    @Test
    void roundtrip() {
        CmsInt24U a = getCmsInt24U().value(100000);
        CmsInt24U b = getCmsInt24U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxValue() {
        int max = (1 << 24) - 1;
        CmsInt24U a = getCmsInt24U().value(max);
        CmsInt24U b = getCmsInt24U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt24U a = getCmsInt24U().value(0);
        CmsInt24U b = getCmsInt24U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt24U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt24U src = getCmsInt24U().value(999999);
        CmsInt24U target = getCmsInt24U().decode(src.encode());
        assertEquals(src, target);
    }
}
