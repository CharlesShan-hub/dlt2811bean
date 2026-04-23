package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CmsOrCat")
class CmsOrCatTest {

    @Test
    @DisplayName("construct, set, is, encode/decode")
    void testAll() throws Exception {
        CmsOrCat cat = new CmsOrCat();
        assertEquals(CmsOrCat.NOT_SUPPORTED, cat.get());

        cat.set(CmsOrCat.BAY_CONTROL);
        assertTrue(cat.is(CmsOrCat.BAY_CONTROL));
        assertFalse(cat.is(CmsOrCat.REMOTE_CONTROL));

        PerOutputStream pos = new PerOutputStream();
        cat.encode(pos);

        CmsOrCat decoded = new CmsOrCat().decode(new PerInputStream(pos.toByteArray()));
        assertEquals(CmsOrCat.BAY_CONTROL, decoded.get());
        assertTrue(decoded.is(CmsOrCat.BAY_CONTROL));
    }
}
