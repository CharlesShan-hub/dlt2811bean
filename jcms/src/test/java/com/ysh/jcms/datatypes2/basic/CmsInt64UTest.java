package com.ysh.jcms.datatypes2.basic;

import com.ysh.jcms.datatypes2.data.basic.CmsInt64U;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64U")
class CmsInt64UTest {

    @Test
    void roundtrip() {
        CmsInt64U original = new CmsInt64U(1234567890123L);
        byte[] data = original.encode();
        CmsInt64U decoded = CmsInt64U.from(data);
        assertEquals(original.longValue(), decoded.longValue());
    }
}
