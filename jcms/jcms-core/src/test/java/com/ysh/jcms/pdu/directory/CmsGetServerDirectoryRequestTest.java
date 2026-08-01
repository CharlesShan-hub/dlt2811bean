package com.ysh.jcms.pdu.directory;

import com.ysh.jcms.data.enumerate.CmsObjectClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryRequestTest {
    @Test
    public void basic() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest()
            .objectClass(CmsObjectClass.LOGICAL_DEVICE);
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void withReferenceAfter() {
        CmsGetServerDirectoryRequest a = new CmsGetServerDirectoryRequest()
            .objectClass(CmsObjectClass.FILE_SYSTEM)
            .referenceAfter("myRef");
        byte[] encoded = a.encode();

        CmsGetServerDirectoryRequest b = new CmsGetServerDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
