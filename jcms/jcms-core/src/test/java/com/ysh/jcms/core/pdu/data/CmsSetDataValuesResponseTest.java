package com.ysh.jcms.core.pdu.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetDataValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetDataValuesResponse a = new CmsSetDataValuesResponse();
        byte[] encoded = a.encode();

        CmsSetDataValuesResponse b = new CmsSetDataValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
