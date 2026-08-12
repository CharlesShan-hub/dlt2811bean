package com.ysh.jcms.core.pdu.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectErrorTest {
    @Test
    public void roundup() {
        CmsSelectError a = new CmsSelectError().reference("err1".getBytes());
        byte[] encoded = a.encode();

        CmsSelectError b = new CmsSelectError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
