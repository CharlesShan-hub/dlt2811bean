package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat32")
class CmsFloat32Test {

    private CmsFloat32 getCmsFloat32() {
        return (CmsFloat32)(new CmsFloat32().test());
    }

    @Test
    void roundtrip() {
        CmsFloat32 a = getCmsFloat32().value(3.14f);
        CmsFloat32 b = getCmsFloat32().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-6f);
    }

    @Test
    void negative() {
        CmsFloat32 a = getCmsFloat32().value(-2.5f);
        CmsFloat32 b = getCmsFloat32().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-6f);
    }

    @Test
    void zero() {
        CmsFloat32 a = getCmsFloat32().value(0.0f);
        CmsFloat32 b = getCmsFloat32().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e-6f);
    }

    @Test
    void largeValue() {
        CmsFloat32 a = getCmsFloat32().value(1e10f);
        CmsFloat32 b = getCmsFloat32().decode(a.encode());
        assertEquals(a.value(), b.value(), 1e5f);
    }

    @Test
    void defaultValue() {
        assertEquals(0.0f, getCmsFloat32().value());
    }

    @Test
    void decodeOverwrites() {
        CmsFloat32 a = getCmsFloat32().value(100.0f);
        CmsFloat32 b = getCmsFloat32().value(42.5f).decode(a.encode());
        assertEquals(b.value(), a.value(), 1e-6f);
    }
}
