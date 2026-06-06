package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt8U")
class CmsInt8UTest {

    @Test
    void roundtrip() {
        CmsInt8U original = new CmsInt8U().value((byte) 100);
        assertEquals(original, new CmsInt8U().decode(original.encode()));
    }

    @Test
    void maxByte() {
        CmsInt8U v = new CmsInt8U().value((byte) 127);
        assertEquals(v, new CmsInt8U().decode(v.encode()));
    }

    @Test
    void zero() {
        CmsInt8U v = new CmsInt8U().value((byte) 0);
        assertEquals(v, new CmsInt8U().decode(v.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt8U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt8U target = new CmsInt8U().value((byte) 55);
        target.decode(new CmsInt8U().value((byte) 33).encode());
        assertEquals(new CmsInt8U().value((byte) 33), target);
    }
}
