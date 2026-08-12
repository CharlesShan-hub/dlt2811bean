package com.ysh.jcms.core.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt32UTest {
    @Test
    public void roundup() {
        CmsInt32U a = new CmsInt32U(3000000000L);
        byte[] encoded = a.encode();
        CmsInt32U b = new CmsInt32U();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
