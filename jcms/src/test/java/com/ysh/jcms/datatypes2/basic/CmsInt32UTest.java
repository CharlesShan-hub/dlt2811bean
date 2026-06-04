package com.ysh.jcms.datatypes2.basic;

import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt32U")
class CmsInt32UTest {

    @Test
    void roundtrip() {
        CmsInt32U original = new CmsInt32U(3000000000L);
        byte[] data = original.encode();
        CmsInt32U decoded = CmsInt32U.from(data);
        assertEquals(original.longValue(), decoded.longValue());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt32U(0L).encode();
        CmsInt32U r = CmsInt32U.from(data);
        assertEquals(0L, r.longValue());
    }

    @Test
    void defaultValue() {
        assertEquals(0L, new CmsInt32U().longValue());
    }

    @Test
    void decodeOverwrites() {
        CmsInt32U v = new CmsInt32U(999L);
        v.decode(new CmsInt32U(42L).encode());
        assertEquals(42L, v.longValue());
    }
}
