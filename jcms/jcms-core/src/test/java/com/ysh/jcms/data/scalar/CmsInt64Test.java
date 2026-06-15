package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt64Test {
    @Test
    public void roundtrip() {
        CmsInt64 a = new CmsInt64(-123456789012345L);
        byte[] encoded = a.encode();
        CmsInt64 b = new CmsInt64();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
