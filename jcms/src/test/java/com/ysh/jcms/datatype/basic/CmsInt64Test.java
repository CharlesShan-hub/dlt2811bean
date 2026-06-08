package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    private CmsInt64 getCmsInt64() {
        return (CmsInt64)(new CmsInt64().test());
    }

    @Test
    void roundtrip() {
        CmsInt64 a = getCmsInt64().value(1234567890123L);
        CmsInt64 b = getCmsInt64().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void negative() {
        CmsInt64 a = getCmsInt64().value(-100L);
        CmsInt64 b = getCmsInt64().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void minValue() {
        CmsInt64 a = getCmsInt64().value(Long.MIN_VALUE);
        CmsInt64 b = getCmsInt64().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxValue() {
        CmsInt64 a = getCmsInt64().value(Long.MAX_VALUE);
        CmsInt64 b = getCmsInt64().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt64 a = getCmsInt64().value(0L);
        CmsInt64 b = getCmsInt64().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt64().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt64 src = getCmsInt64().value(999L);
        CmsInt64 target = getCmsInt64().decode(src.encode());
        assertEquals(src, target);
    }
}
