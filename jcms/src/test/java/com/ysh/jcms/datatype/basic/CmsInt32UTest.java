package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    @Test
    void roundtrip() {
        CmsInt32U original = new CmsInt32U().value(300000000);
        byte[] data = original.encode();
        CmsInt32U decoded = new CmsInt32U().decode(data);
        assertEquals(300000000, decoded.value());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt32U().value(0).encode();
        CmsInt32U r = new CmsInt32U().decode(data);
        assertEquals(0, r.value());
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsInt32U().value());
    }

    @Test
    void decodeOverwrites() {
        CmsInt32U v = new CmsInt32U().value(999);
        v.decode(new CmsInt32U().value(42).encode());
        assertEquals(42, v.value());
    }
}
