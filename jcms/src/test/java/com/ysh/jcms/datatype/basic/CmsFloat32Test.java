package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat32")
class CmsFloat32Test {

    @Test
    void roundtrip() {
        CmsFloat32 original = new CmsFloat32().value(3.14f);
        CmsFloat32 decoded = new CmsFloat32().decode(original.encode());
        assertEquals(original.value(), decoded.value(), 1e-6f);
    }

    @Test
    void negative() {
        CmsFloat32 v = new CmsFloat32().value(-2.5f);
        CmsFloat32 decoded = new CmsFloat32().decode(v.encode());
        assertEquals(v.value(), decoded.value(), 1e-6f);
    }

    @Test
    void zero() {
        CmsFloat32 v = new CmsFloat32().value(0.0f);
        CmsFloat32 decoded = new CmsFloat32().decode(v.encode());
        assertEquals(v.value(), decoded.value());
    }

    @Test
    void largeValue() {
        CmsFloat32 v = new CmsFloat32().value(1e10f);
        CmsFloat32 decoded = new CmsFloat32().decode(v.encode());
        assertEquals(v.value(), decoded.value(), 1e5f);
    }

    @Test
    void defaultValue() {
        assertEquals(0.0f, new CmsFloat32().value());
    }

    @Test
    void decodeOverwrites() {
        CmsFloat32 target = new CmsFloat32().value(100.0f);
        target.decode(new CmsFloat32().value(42.5f).encode());
        assertEquals(42.5f, target.value(), 1e-6f);
    }
}
