package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOrCat")
class CmsOrCatTest {

    @Test
    void roundtrip() {
        CmsOrCat original = new CmsOrCat(1);
        byte[] data = original.encode();
        CmsOrCat decoded = CmsOrCat.decode(data);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsOrCat().get());
    }
}
