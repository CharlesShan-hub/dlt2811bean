package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt16Test {
    @Test
    public void roundtrip() {
        CmsInt16 a = new CmsInt16(-12345);
        byte[] encoded = a.encode();
        CmsInt16 b = new CmsInt16();
        b.decode(encoded);
        assertEquals(-12345, b.value());
    }
}
