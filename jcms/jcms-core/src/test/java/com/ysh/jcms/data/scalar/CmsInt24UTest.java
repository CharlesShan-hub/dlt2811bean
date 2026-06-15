package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt24UTest {
    @Test
    public void roundtrip() {
        CmsInt24U a = new CmsInt24U(12345678);
        byte[] encoded = a.encode();
        CmsInt24U b = new CmsInt24U();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
