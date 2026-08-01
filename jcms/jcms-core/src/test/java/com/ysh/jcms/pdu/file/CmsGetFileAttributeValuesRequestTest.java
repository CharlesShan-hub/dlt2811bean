package com.ysh.jcms.pdu.file;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetFileAttributeValuesRequestTest {
    @Test
    public void roundup() {
        CmsGetFileAttributeValuesRequest a = new CmsGetFileAttributeValuesRequest()
            .filename("attr.txt");
        byte[] encoded = a.encode();

        CmsGetFileAttributeValuesRequest b = new CmsGetFileAttributeValuesRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
