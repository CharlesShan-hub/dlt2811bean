package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    private CmsInt32U getCmsInt32U() {
        return (CmsInt32U)(new CmsInt32U().test());
    }

    @Test
    void roundtrip() {
        CmsInt32U a = getCmsInt32U().value(300000000);
        CmsInt32U b = getCmsInt32U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void zero() {
        CmsInt32U a = getCmsInt32U().value(0);
        CmsInt32U b = getCmsInt32U().decode(a.encode());
        assertEquals(a, b);
    }

    @Test
    void defaultValue() {
        assertEquals(0, getCmsInt32U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt32U src = getCmsInt32U().value(999);
        CmsInt32U target = getCmsInt32U().decode(src.encode());
        assertEquals(src, target);
    }
}
