package com.ysh.jcms.data.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsOriginatorTest {
    @Test
    public void roundup() {
        CmsOriginator a = new CmsOriginator().orCat(CmsOriginator.OR_CAT_BAY_CONTROL).orIdent("testIdent".getBytes());
        byte[] encoded = a.encode();
        CmsOriginator b = new CmsOriginator();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
