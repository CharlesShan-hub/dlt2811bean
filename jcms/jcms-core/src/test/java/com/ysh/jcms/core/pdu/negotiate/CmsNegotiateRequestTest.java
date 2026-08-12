package com.ysh.jcms.core.pdu.negotiate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsNegotiateRequestTest {
    @Test
    public void roundup() {
        CmsNegotiateRequest a = new CmsNegotiateRequest()
            .apduSize(1024)
            .asduSize(65536L)
            .protocolVersion(1L);
        byte[] encoded = a.encode();

        CmsNegotiateRequest b = new CmsNegotiateRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
