package com.ysh.jcms.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileResponseTest {
    @Test
    public void roundup() {
        CmsGetFileResponse a = new CmsGetFileResponse()
            .fileData("file content".getBytes())
            .endOfFile(true);
        byte[] encoded = a.encode();

        CmsGetFileResponse b = new CmsGetFileResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
