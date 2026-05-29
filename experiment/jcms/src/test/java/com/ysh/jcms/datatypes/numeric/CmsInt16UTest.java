package com.ysh.jcms.datatypes.numeric;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsInt16U")
class CmsInt16UTest {

    @Test
    void positive() {
        byte[] data = new CmsInt16U(60000).encode();
        CmsInt16U r = CmsInt16U.decode(data);
        assertEquals(60000, (int) r.get());
    }

    @Test
    void zero() {
        byte[] data = new CmsInt16U(0).encode();
        CmsInt16U r = CmsInt16U.decode(data);
        assertEquals(0, (int) r.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsInt16U().get());
    }

    @Test
    void roundtrip() {
        CmsInt16U original = new CmsInt16U(60000);
        byte[] data = original.encode();
        CmsInt16U decoded = CmsInt16U.decode(data);
        assertEquals(original.get(), decoded.get());
    }
}
