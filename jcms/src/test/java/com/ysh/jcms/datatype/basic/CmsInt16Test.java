package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16")
class CmsInt16Test {

    @Test
    void roundtrip() {
        CmsInt16 original = new CmsInt16().value((short) 12345);
        assertEquals(original, new CmsInt16().decode(original.encode()));
    }

    @Test
    void negative() {
        CmsInt16 v = new CmsInt16().value((short) -20000);
        assertEquals(v, new CmsInt16().decode(v.encode()));
    }

    @Test
    void minValue() {
        CmsInt16 v = new CmsInt16().value(Short.MIN_VALUE);
        assertEquals(v, new CmsInt16().decode(v.encode()));
    }

    @Test
    void maxValue() {
        CmsInt16 v = new CmsInt16().value(Short.MAX_VALUE);
        assertEquals(v, new CmsInt16().decode(v.encode()));
    }

    @Test
    void zero() {
        CmsInt16 v = new CmsInt16().value((short) 0);
        assertEquals(v, new CmsInt16().decode(v.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt16().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt16 target = new CmsInt16().value((short) 999);
        target.decode(new CmsInt16().value((short) -1).encode());
        assertEquals(new CmsInt16().value((short) -1), target);
    }
}
