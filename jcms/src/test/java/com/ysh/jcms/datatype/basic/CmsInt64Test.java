package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    @Test
    void roundtrip() {
        CmsInt64 original = new CmsInt64().value(1234567890123L);
        byte[] data = original.encode();
        CmsInt64 decoded = new CmsInt64().decode(data);
        assertEquals(1234567890123L, decoded.value());
    }

    @Test
    void negative() {
        byte[] data = new CmsInt64().value(-100L).encode();
        CmsInt64 r = new CmsInt64().decode(data);
        assertEquals(-100L, r.value());
    }
}
