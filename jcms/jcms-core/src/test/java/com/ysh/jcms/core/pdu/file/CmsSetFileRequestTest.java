package com.ysh.jcms.core.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsSetFileRequestTest {
    @Test
    public void roundup() {
        CmsSetFileRequest a = new CmsSetFileRequest()
            .filename("new.txt")
            .startPosition(0L)
            .fileData("new content".getBytes())
            .endOfFile(true);
        byte[] encoded = a.encode();

        CmsSetFileRequest b = new CmsSetFileRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
