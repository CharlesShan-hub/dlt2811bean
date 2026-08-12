package com.ysh.jcms.core.pdu.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectResponseTest {
    @Test
    public void roundup() {
        CmsSelectResponse a = new CmsSelectResponse().reference("resp1".getBytes());
        byte[] encoded = a.encode();

        CmsSelectResponse b = new CmsSelectResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
