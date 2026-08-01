package com.ysh.jcms.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsConfirmEditSgValuesResponseTest {
    @Test
    public void roundup() {
        CmsConfirmEditSgValuesResponse a = new CmsConfirmEditSgValuesResponse();
        byte[] encoded = a.encode();

        CmsConfirmEditSgValuesResponse b = new CmsConfirmEditSgValuesResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
