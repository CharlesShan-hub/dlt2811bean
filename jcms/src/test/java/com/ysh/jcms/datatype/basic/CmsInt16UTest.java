package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16U")
class CmsInt16UTest {

    @Test
    void roundtrip() {
        CmsInt16U original = new CmsInt16U().value((short) 12345);
        assertEquals(original, new CmsInt16U().decode(original.encode()));
    }

    @Test
    void zero() {
        CmsInt16U v = new CmsInt16U().value((short) 0);
        assertEquals(v, new CmsInt16U().decode(v.encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt16U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt16U target = new CmsInt16U().value((short) 32767);
        target.decode(new CmsInt16U().value((short) 42).encode());
        assertEquals(new CmsInt16U().value((short) 42), target);
    }
}
