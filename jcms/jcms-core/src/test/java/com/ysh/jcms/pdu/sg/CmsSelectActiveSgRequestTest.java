package com.ysh.jcms.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectActiveSgRequestTest {
    @Test
    public void roundup() {
        CmsSelectActiveSgRequest a = new CmsSelectActiveSgRequest()
            .sgcbReference("sgcbRef")
            .settingGroupNumber(1);
        byte[] encoded = a.encode();

        CmsSelectActiveSgRequest b = new CmsSelectActiveSgRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
