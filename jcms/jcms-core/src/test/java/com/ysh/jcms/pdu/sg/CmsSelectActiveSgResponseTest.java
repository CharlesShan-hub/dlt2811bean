package com.ysh.jcms.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectActiveSgResponseTest {
    @Test
    public void roundup() {
        CmsSelectActiveSgResponse a = new CmsSelectActiveSgResponse();
        byte[] encoded = a.encode();

        CmsSelectActiveSgResponse b = new CmsSelectActiveSgResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
