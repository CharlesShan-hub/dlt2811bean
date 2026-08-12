package com.ysh.jcms.core.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt8Test {
    @Test
    public void roundup() {
        CmsInt8 a = new CmsInt8(-42);
        byte[] encoded = a.encode();
        CmsInt8 b = new CmsInt8();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
