package com.ysh.jcms.core.pdu.goose;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetGoCbValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetGoCbValuesResponse a = new CmsSetGoCbValuesResponse();
        byte[] encoded = a.encode();

        CmsSetGoCbValuesResponse b = new CmsSetGoCbValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
