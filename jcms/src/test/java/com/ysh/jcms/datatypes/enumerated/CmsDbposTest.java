package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsDbpos")
class CmsDbposTest {

    @Test
    void roundtrip() {
        CmsDbpos original = new CmsDbpos(2);
        byte[] data = original.encode();
        CmsDbpos decoded = CmsDbpos.from(data);
        //System.out.println(decoded);
        assertEquals(original.get(), decoded.get());
    }

    @Test
    void defaultValue() {
        assertEquals(0, new CmsDbpos().get());
    }

    @Test
    void copy() {
        CmsDbpos original = new CmsDbpos(1);
        CmsDbpos cloned = original.copy();
        assertEquals(original.get(), cloned.get());
    }
}
