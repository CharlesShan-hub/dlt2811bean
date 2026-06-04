package com.ysh.jcms.datatypes.enumerated;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOrCat")
class CmsOrCatTest {

    @Test
    void defaultValue() {
        assertEquals(0, (int) new CmsOrCat().get());
    }

    @Test
    void constructWithValue() {
        CmsOrCat cat = new CmsOrCat(5);
        assertEquals(5, cat.get());
    }

    @Test
    void copy() {
        CmsOrCat cat = new CmsOrCat(3);
        CmsOrCat cloned = cat.copy();
        assertEquals(cat.get(), cloned.get());
    }
}
