package com.ysh.jcms.core.pdu.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectRequestTest {
    @Test
    public void roundup() {
        CmsSelectRequest a = new CmsSelectRequest().reference("ref1".getBytes());
        byte[] encoded = a.encode();

        CmsSelectRequest b = new CmsSelectRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
