package com.ysh.jcms.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsDeleteFileRequestTest {
    @Test
    public void roundup() {
        CmsDeleteFileRequest a = new CmsDeleteFileRequest()
            .filename("del.txt");
        byte[] encoded = a.encode();

        CmsDeleteFileRequest b = new CmsDeleteFileRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
