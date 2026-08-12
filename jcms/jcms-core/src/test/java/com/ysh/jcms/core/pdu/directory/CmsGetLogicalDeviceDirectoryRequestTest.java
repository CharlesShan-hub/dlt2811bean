package com.ysh.jcms.core.pdu.directory;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryRequestTest {
    @Test
    public void withoutOptional() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest();
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }

    @Test
    public void withLdName() {
        CmsGetLogicalDeviceDirectoryRequest a = new CmsGetLogicalDeviceDirectoryRequest()
            .ldName("ld1");
        byte[] encoded = a.encode();

        CmsGetLogicalDeviceDirectoryRequest b = new CmsGetLogicalDeviceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
