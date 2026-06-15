package com.ysh.jcms.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsFloat64Test {
    @Test
    public void roundup() {
        CmsFloat64 a = new CmsFloat64(3.14159265358979);
        byte[] encoded = a.encode();
        CmsFloat64 b = new CmsFloat64();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
