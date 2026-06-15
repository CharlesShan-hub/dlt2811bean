package com.ysh.jcms.data.block;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsLcbOptFldsTest {
    @Test
    public void roundtrip() {
        CmsLcbOptFlds a = new CmsLcbOptFlds();
        a.value.value(true);
        byte[] encoded = a.encode();
        CmsLcbOptFlds b = new CmsLcbOptFlds();
        b.decode(encoded);
        assertTrue(b.value.value());
    }
}
