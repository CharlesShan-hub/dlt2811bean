package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16U")
class CmsInt16UTest {

    private CmsInt16U getCmsInt16U() {
        return (CmsInt16U)(new CmsInt16U().test());
    }

    @Test
    void roundtrip() {
        CmsInt16U a = getCmsInt16U().value((short) 12345);
        CmsInt16U b = getCmsInt16U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt16U a = getCmsInt16U().value((short) 0);
        CmsInt16U b = getCmsInt16U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt16U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt16U src = getCmsInt16U().value((short) 32767);
        CmsInt16U target = getCmsInt16U().decode(src.encode());
        assertEquals(src, target);
    }
}
