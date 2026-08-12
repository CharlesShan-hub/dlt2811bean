package com.ysh.jcms.core.pdu.log;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetLcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetLcbValuesResponse a = new CmsSetLcbValuesResponse();
        byte[] encoded = a.encode();

        CmsSetLcbValuesResponse b = new CmsSetLcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
