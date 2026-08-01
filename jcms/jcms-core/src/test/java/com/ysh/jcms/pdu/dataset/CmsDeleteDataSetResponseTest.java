package com.ysh.jcms.pdu.dataset;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDeleteDataSetResponseTest {
    @Test
    public void roundup() {
        CmsDeleteDataSetResponse a = new CmsDeleteDataSetResponse();
        byte[] encoded = a.encode();

        CmsDeleteDataSetResponse b = new CmsDeleteDataSetResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
