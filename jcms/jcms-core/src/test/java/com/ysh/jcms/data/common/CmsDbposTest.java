package com.ysh.jcms.data.common;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDbposTest {
    @Test
    public void roundtrip() {
        CmsDbpos a = new CmsDbpos(CmsDbpos.ON);
        byte[] encoded = a.encode();
        CmsDbpos b = new CmsDbpos();
        b.decode(encoded);
        assertEquals(CmsDbpos.ON, b.value());
    }
}
