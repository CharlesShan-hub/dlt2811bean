package com.ysh.jcms.core.pdu.sg;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSelectEditSgRequestTest {
    @Test
    public void roundup() {
        CmsSelectEditSgRequest a = new CmsSelectEditSgRequest()
            .sgcbReference("editSgcbRef")
            .settingGroupNumber(2);
        byte[] encoded = a.encode();

        CmsSelectEditSgRequest b = new CmsSelectEditSgRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
