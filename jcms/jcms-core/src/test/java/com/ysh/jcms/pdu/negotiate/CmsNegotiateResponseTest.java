package com.ysh.jcms.pdu.negotiate;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsNegotiateResponseTest {
    @Test
    public void roundup() {
        CmsNegotiateResponse a = new CmsNegotiateResponse()
            .apduSize(2048)
            .asduSize(131072L)
            .protocolVersion(2L)
            .modelVersion("1.0");
        byte[] encoded = a.encode();

        CmsNegotiateResponse b = new CmsNegotiateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
