package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32")
class CmsInt32Test {

    private CmsInt32 getCmsInt32() {
        return (CmsInt32)(new CmsInt32().test());
    }

    @Test
    void roundtrip() {
        CmsInt32 a = getCmsInt32().value(123456789);
        CmsInt32 b = getCmsInt32().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void negative() {
        CmsInt32 a = getCmsInt32().value(-500000);
        CmsInt32 b = getCmsInt32().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void minValue() {
        CmsInt32 a = getCmsInt32().value(Integer.MIN_VALUE);
        CmsInt32 b = getCmsInt32().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxValue() {
        CmsInt32 a = getCmsInt32().value(Integer.MAX_VALUE);
        CmsInt32 b = getCmsInt32().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt32 a = getCmsInt32().value(0);
        CmsInt32 b = getCmsInt32().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt32().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt32 src = getCmsInt32().value(999);
        CmsInt32 target = getCmsInt32().decode(src.encode());
        assertEquals(src, target);
    }
}
