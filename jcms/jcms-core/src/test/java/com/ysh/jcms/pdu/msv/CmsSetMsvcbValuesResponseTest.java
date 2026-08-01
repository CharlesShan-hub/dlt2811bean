package com.ysh.jcms.pdu.msv;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetMsvcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetMsvcbValuesResponse a = new CmsSetMsvcbValuesResponse();
        byte[] encoded = a.encode();

        CmsSetMsvcbValuesResponse b = new CmsSetMsvcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
