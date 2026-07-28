package com.ysh.jcms.svc.connection;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReleaseRequestTest {
    @Test
    public void roundup() {
        CmsReleaseRequest a = new CmsReleaseRequest().associationId(new byte[]{0x0A, 0x0B, 0x0C, 0x0D});
        byte[] encoded = a.encode();

        CmsReleaseRequest b = new CmsReleaseRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
