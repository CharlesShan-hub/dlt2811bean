package com.ysh.jcms.data.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsOrCatTest {
    @Test
    public void roundtrip() {
        CmsOrCat a = new CmsOrCat(CmsOrCat.STATION_CONTROL);
        byte[] encoded = a.encode();
        CmsOrCat b = new CmsOrCat();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
