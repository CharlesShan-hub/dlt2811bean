package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt16UTest {
    @Test
    public void roundtrip() {
        CmsInt16U a = new CmsInt16U(60000);
        byte[] encoded = a.encode();
        CmsInt16U b = new CmsInt16U();
        b.decode(encoded);
        assertEquals(60000, b.value());
    }
}
