package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat64")
class CmsFloat64Test {

    private CmsFloat64 getCmsFloat64() {
        return (CmsFloat64)(new CmsFloat64().test());
    }

    @Test
    void roundtrip() {
        CmsFloat64 a = getCmsFloat64().value(3.14159265358979);
        CmsFloat64 b = getCmsFloat64().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-14);
    }

    @Test
    void negative() {
        CmsFloat64 a = getCmsFloat64().value(-1e-5);
        CmsFloat64 b = getCmsFloat64().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-14);
    }

    @Test
    void zero() {
        CmsFloat64 a = getCmsFloat64().value(0.0);
        CmsFloat64 b = getCmsFloat64().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-14);
    }

    @Test
    void largeValue() {
        CmsFloat64 a = getCmsFloat64().value(1e15);
        CmsFloat64 b = getCmsFloat64().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-5);
    }

    @Test
    void defaultValue() {
        assertEquals(0.0, getCmsFloat64().value());
    }

    @Test
    void decodeOverwrites() {
        CmsFloat64 a = getCmsFloat64().value(999.999);
        CmsFloat64 b = getCmsFloat64().value(-0.5);
        a.decode(b.encode());
        assertEquals(b.value(), a.value(), 1e-14);
    }
}
