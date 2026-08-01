package com.ysh.jcms.pdu.report;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetBrcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetBrcbValuesResponse a = new CmsSetBrcbValuesResponse();
        byte[] encoded = a.encode();

        CmsSetBrcbValuesResponse b = new CmsSetBrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
