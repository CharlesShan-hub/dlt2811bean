package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8")
class CmsInt8Test {

    @Test
    void roundtrip() {
        CmsInt8 original = new CmsInt8().value((byte) 42);
        assertEquals(original, new CmsInt8().decode(original.encode()));
    }

    @Test
    void negative() {
        assertEquals(new CmsInt8().value((byte) -100),
                     new CmsInt8().decode(new CmsInt8().value((byte) -100).encode()));
    }

    @Test
    void minValue() {
        CmsInt8 v = new CmsInt8().value(Byte.MIN_VALUE);
        assertEquals(v, new CmsInt8().decode(v.encode()));
    }

    @Test
    void maxValue() {
        CmsInt8 v = new CmsInt8().value(Byte.MAX_VALUE);
        assertEquals(v, new CmsInt8().decode(v.encode()));
    }

    @Test
    void zero() {
        CmsInt8 v = new CmsInt8().value((byte) 0);
        assertEquals(v, new CmsInt8().decode(v.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt8().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt8 target = new CmsInt8().value((byte) 99);
        target.decode(new CmsInt8().value((byte) -1).encode());
        assertEquals(new CmsInt8().value((byte) -1), target);
    }
}
