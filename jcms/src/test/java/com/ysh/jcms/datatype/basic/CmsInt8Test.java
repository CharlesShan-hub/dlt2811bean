package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8")
class CmsInt8Test {

    private CmsInt8 getCmsInt8() {
        return (CmsInt8)(new CmsInt8().test());
    }

    @Test
    void roundtrip() {
        CmsInt8 a = getCmsInt8().value((byte) 42);
        CmsInt8 b = getCmsInt8().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void negative() {
        CmsInt8 a = getCmsInt8().value((byte) -100);
        CmsInt8 b = getCmsInt8().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void minValue() {
        CmsInt8 a = getCmsInt8().value(Byte.MIN_VALUE);
        CmsInt8 b = getCmsInt8().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxValue() {
        CmsInt8 a = getCmsInt8().value(Byte.MAX_VALUE);
        CmsInt8 b = getCmsInt8().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt8 a = getCmsInt8().value((byte) 0);
        CmsInt8 b = getCmsInt8().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt8().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt8 src = getCmsInt8().value((byte) 99);
        CmsInt8 target = getCmsInt8().decode(src.encode());
        assertEquals(src, target);
    }
}
