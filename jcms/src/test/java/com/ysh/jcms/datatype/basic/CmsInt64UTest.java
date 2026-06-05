package com.ysh.jcms.datatype.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64U")
class CmsInt64UTest {

    @Test
    void roundtrip() {
        CmsInt64U original = new CmsInt64U().value(1234567890123L);
        byte[] data = original.encode();
        CmsInt64U decoded = new CmsInt64U().decode(data);
        assertEquals(1234567890123L, decoded.value());
    }
}
