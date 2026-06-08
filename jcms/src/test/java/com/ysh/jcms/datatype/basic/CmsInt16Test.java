package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16")
class CmsInt16Test {

    private CmsInt16 getCmsInt16() {
        return (CmsInt16)(new CmsInt16().test());
    }

    @Test
    void roundtrip() {
        CmsInt16 a = getCmsInt16().value((short) 12345);
        CmsInt16 b = getCmsInt16().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void negative() {
        CmsInt16 a = getCmsInt16().value((short) -20000);
        CmsInt16 b = getCmsInt16().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void minValue() {
        CmsInt16 a = getCmsInt16().value(Short.MIN_VALUE);
        CmsInt16 b = getCmsInt16().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxValue() {
        CmsInt16 a = getCmsInt16().value(Short.MAX_VALUE);
        CmsInt16 b = getCmsInt16().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt16 a = getCmsInt16().value((short) 0);
        CmsInt16 b = getCmsInt16().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt16().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt16 src = getCmsInt16().value((short) 999);
        CmsInt16 target = getCmsInt16().decode(src.encode());
        assertEquals(src, target);
    }
}
