package com.ysh.jcms.data.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsOriginatorTest {
    @Test
    public void roundtrip() {
        CmsOriginator a = new CmsOriginator();
        a.orCat.value(CmsOrCat.BAY_CONTROL);
        a.orIdent.value("testIdent".getBytes());
        byte[] encoded = a.encode();
        CmsOriginator b = new CmsOriginator();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
