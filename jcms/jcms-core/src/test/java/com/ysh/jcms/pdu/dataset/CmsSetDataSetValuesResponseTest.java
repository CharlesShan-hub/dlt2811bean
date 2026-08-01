package com.ysh.jcms.pdu.dataset;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetDataSetValuesResponseTest {
    @Test
    public void roundup() {
        CmsSetDataSetValuesResponse a = new CmsSetDataSetValuesResponse();
        byte[] encoded = a.encode();

        CmsSetDataSetValuesResponse b = new CmsSetDataSetValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
