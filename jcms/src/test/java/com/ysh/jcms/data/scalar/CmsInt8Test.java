package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsInt8Test {
    @Test
    public void roundtrip() {
        CmsInt8 a = new CmsInt8(-42);
        byte[] encoded = a.encode();
        CmsInt8 b = new CmsInt8();
        b.decode(encoded);
        assertEquals(-42, b.value());
    }
}
