package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8U")
class CmsInt8UTest {

    private CmsInt8U getCmsInt8U() {
        return (CmsInt8U)(new CmsInt8U().test());
    }

    @Test
    void roundtrip() {
        CmsInt8U a = getCmsInt8U().value((byte) 100);
        CmsInt8U b = getCmsInt8U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void maxByte() {
        CmsInt8U a = getCmsInt8U().value((byte) 127);
        CmsInt8U b = getCmsInt8U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt8U a = getCmsInt8U().value((byte) 0);
        CmsInt8U b = getCmsInt8U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt8U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt8U src = getCmsInt8U().value((byte) 55);
        CmsInt8U target = getCmsInt8U().decode(src.encode());
        assertEquals(src, target);
    }
}
