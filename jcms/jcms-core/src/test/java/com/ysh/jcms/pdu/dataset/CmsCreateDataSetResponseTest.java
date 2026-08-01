package com.ysh.jcms.pdu.dataset;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsCreateDataSetResponseTest {
    @Test
    public void roundup() {
        CmsCreateDataSetResponse a = new CmsCreateDataSetResponse();
        byte[] encoded = a.encode();

        CmsCreateDataSetResponse b = new CmsCreateDataSetResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
