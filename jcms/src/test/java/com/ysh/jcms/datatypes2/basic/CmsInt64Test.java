package com.ysh.jcms.datatypes2.basic;

import com.ysh.jcms.datatypes2.data.basic.CmsInt64;
import com.ysh.jcms.datatypes2.data.basic.CmsInt64U;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt64")
class CmsInt64Test {

    @Test
    void roundtrip() {
        CmsInt64 original = new CmsInt64(1234567890123L);
        byte[] data = original.encode();
        CmsInt64 decoded = CmsInt64.from(data);
        assertEquals(original.longValue(), decoded.longValue());
    }

    @Test
    void negative() {
        byte[] data = new CmsInt64(-100L).encode();
        CmsInt64 r = CmsInt64.from(data);
        assertEquals(-100L, r.longValue());
    }
}
