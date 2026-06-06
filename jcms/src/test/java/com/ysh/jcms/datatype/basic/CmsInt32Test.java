package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32")
class CmsInt32Test {

    @Test
    void roundtrip() {
        CmsInt32 original = new CmsInt32().value(123456789);
        assertEquals(original, new CmsInt32().decode(original.encode()));
    }

    @Test
    void negative() {
        assertEquals(new CmsInt32().value(-500000),
                     new CmsInt32().decode(new CmsInt32().value(-500000).encode()));
    }

    @Test
    void minValue() {
        CmsInt32 v = new CmsInt32().value(Integer.MIN_VALUE);
        assertEquals(v, new CmsInt32().decode(v.encode()));
    }

    @Test
    void maxValue() {
        CmsInt32 v = new CmsInt32().value(Integer.MAX_VALUE);
        assertEquals(v, new CmsInt32().decode(v.encode()));
    }

    @Test
    void zero() {
        assertEquals(new CmsInt32().value(0),
                     new CmsInt32().decode(new CmsInt32().value(0).encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt32().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt32 target = new CmsInt32().value(999);
        target.decode(new CmsInt32().value(-1).encode());
        assertEquals(new CmsInt32().value(-1), target);
    }
}
