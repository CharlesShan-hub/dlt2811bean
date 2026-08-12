package com.ysh.jcms.core.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsConfirmEditSgValuesRequestTest {
    @Test
    public void roundup() {
        CmsConfirmEditSgValuesRequest a = new CmsConfirmEditSgValuesRequest()
            .sgcbReference("sgcbRef");
        byte[] encoded = a.encode();

        CmsConfirmEditSgValuesRequest b = new CmsConfirmEditSgValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
