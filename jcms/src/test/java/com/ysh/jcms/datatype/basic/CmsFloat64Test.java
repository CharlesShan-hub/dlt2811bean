package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsFloat64")
class CmsFloat64Test {

    @Test
    void roundtrip() {
        CmsFloat64 original = new CmsFloat64().value(3.14159265358979);
        CmsFloat64 decoded = new CmsFloat64().decode(original.encode());
        assertEquals(original.value(), decoded.value(), 1e-14);
    }

    @Test
    void negative() {
        CmsFloat64 v = new CmsFloat64().value(-1e-5);
        CmsFloat64 decoded = new CmsFloat64().decode(v.encode());
        assertEquals(v.value(), decoded.value(), 1e-14);
    }

    @Test
    void zero() {
        CmsFloat64 v = new CmsFloat64().value(0.0);
        CmsFloat64 decoded = new CmsFloat64().decode(v.encode());
        assertEquals(v.value(), decoded.value());
    }

    @Test
    void largeValue() {
        CmsFloat64 v = new CmsFloat64().value(1e15);
        CmsFloat64 decoded = new CmsFloat64().decode(v.encode());
        assertEquals(v.value(), decoded.value(), 1e-5);
    }

    @Test
    void defaultValue() {
        assertEquals(0.0, new CmsFloat64().value());
    }

    @Test
    void decodeOverwrites() {
        CmsFloat64 target = new CmsFloat64().value(999.999);
        target.decode(new CmsFloat64().value(-0.5).encode());
        assertEquals(-0.5, target.value(), 1e-14);
    }
}
