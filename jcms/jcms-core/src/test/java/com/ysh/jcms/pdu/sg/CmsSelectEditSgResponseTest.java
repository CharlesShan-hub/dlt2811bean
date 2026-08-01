package com.ysh.jcms.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectEditSgResponseTest {
    @Test
    public void roundup() {
        CmsSelectEditSgResponse a = new CmsSelectEditSgResponse();
        byte[] encoded = a.encode();

        CmsSelectEditSgResponse b = new CmsSelectEditSgResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
