package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt32Test {
    @Test
    public void roundtrip() {
        CmsInt32 a = new CmsInt32(-1234567);
        byte[] encoded = a.encode();
        CmsInt32 b = new CmsInt32();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
