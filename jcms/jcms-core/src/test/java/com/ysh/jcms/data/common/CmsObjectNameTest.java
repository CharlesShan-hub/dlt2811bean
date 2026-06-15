package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsObjectNameTest {
    @Test
    public void roundup() {
        CmsObjectName a = new CmsObjectName("MyObject");
        byte[] encoded = a.encode();
        CmsObjectName b = new CmsObjectName();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
