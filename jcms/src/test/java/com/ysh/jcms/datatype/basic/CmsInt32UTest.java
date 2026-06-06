package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    @Test
    void roundtrip() {
        CmsInt32U original = new CmsInt32U().value(300000000);
        assertEquals(original, new CmsInt32U().decode(original.encode()));
    }

    @Test
    void zero() {
        assertEquals(new CmsInt32U().value(0),
                     new CmsInt32U().decode(new CmsInt32U().value(0).encode()));
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt32U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt32U target = new CmsInt32U().value(999);
        target.decode(new CmsInt32U().value(42).encode());
        assertEquals(new CmsInt32U().value(42), target);
    }
}
