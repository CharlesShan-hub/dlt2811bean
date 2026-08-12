package com.ysh.jcms.core.data.scalar;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsBooleanTest {
    @Test
    public void roundup() {
        CmsBoolean a = new CmsBoolean(true);
        byte[] encoded = a.encode();
        CmsBoolean b = new CmsBoolean();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
