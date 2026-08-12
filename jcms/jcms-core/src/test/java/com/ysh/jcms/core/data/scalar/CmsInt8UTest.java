package com.ysh.jcms.core.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt8UTest {
    @Test
    public void roundup() {
        CmsInt8U a = new CmsInt8U(200);
        byte[] encoded = a.encode();
        CmsInt8U b = new CmsInt8U();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
