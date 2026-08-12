package com.ysh.jcms.core.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileRequestTest {
    @Test
    public void roundup() {
        CmsGetFileRequest a = new CmsGetFileRequest()
            .filename("test.txt")
            .startPosition(0L);
        byte[] encoded = a.encode();

        CmsGetFileRequest b = new CmsGetFileRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
