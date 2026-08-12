package com.ysh.jcms.core.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetEditSgValueResponseTest {
    @Test
    public void roundup() {
        CmsSetEditSgValueResponse a = new CmsSetEditSgValueResponse();
        byte[] encoded = a.encode();

        CmsSetEditSgValueResponse b = new CmsSetEditSgValueResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
