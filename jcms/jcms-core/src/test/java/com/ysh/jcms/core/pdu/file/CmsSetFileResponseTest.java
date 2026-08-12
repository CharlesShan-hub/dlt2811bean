package com.ysh.jcms.core.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetFileResponseTest {
    @Test
    public void roundup() {
        CmsSetFileResponse a = new CmsSetFileResponse();
        byte[] encoded = a.encode();

        CmsSetFileResponse b = new CmsSetFileResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
