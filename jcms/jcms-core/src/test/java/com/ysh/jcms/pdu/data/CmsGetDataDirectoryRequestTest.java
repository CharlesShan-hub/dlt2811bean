package com.ysh.jcms.pdu.data;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryRequestTest {
    @Test
    public void roundup() {
        CmsGetDataDirectoryRequest a = new CmsGetDataDirectoryRequest()
            .dataReference("dataRef")
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetDataDirectoryRequest b = new CmsGetDataDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
