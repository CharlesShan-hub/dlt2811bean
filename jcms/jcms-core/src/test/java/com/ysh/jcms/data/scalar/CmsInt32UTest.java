package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt32UTest {
    @Test
    public void roundtrip() {
        CmsInt32U a = new CmsInt32U(3000000000L);
        byte[] encoded = a.encode();
        CmsInt32U b = new CmsInt32U();
        b.decode(encoded);
        assertEquals(3000000000L, b.value());
    }
}
