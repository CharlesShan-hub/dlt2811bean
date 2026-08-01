package com.ysh.jcms.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDeleteFileResponseTest {
    @Test
    public void roundup() {
        CmsDeleteFileResponse a = new CmsDeleteFileResponse();
        byte[] encoded = a.encode();

        CmsDeleteFileResponse b = new CmsDeleteFileResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
