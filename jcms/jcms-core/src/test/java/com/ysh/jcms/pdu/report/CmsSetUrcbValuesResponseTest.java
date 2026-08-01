package com.ysh.jcms.pdu.report;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetUrcbValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetUrcbValuesResponse a = new CmsSetUrcbValuesResponse();
        byte[] encoded = a.encode();

        CmsSetUrcbValuesResponse b = new CmsSetUrcbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
