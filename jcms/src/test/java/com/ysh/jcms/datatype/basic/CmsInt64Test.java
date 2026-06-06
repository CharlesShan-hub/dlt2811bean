package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    @Test
    void roundtrip() {
        CmsInt64 original = new CmsInt64().value(1234567890123L);
        assertEquals(original, new CmsInt64().decode(original.encode()));
    }

    @Test
    void negative() {
        assertEquals(new CmsInt64().value(-100L),
                     new CmsInt64().decode(new CmsInt64().value(-100L).encode()));
    }
}
