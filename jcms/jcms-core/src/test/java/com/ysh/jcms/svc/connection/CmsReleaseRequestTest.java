package com.ysh.jcms.svc.connection;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsReleaseRequestTest {
    @Test
    public void roundtrip() {
        CmsReleaseRequest a = new CmsReleaseRequest();
        a.reqId.value(20);
        a.assocId.value(new byte[]{0x0A, 0x0B, 0x0C, 0x0D});
        byte[] encoded = a.encode();

        CmsReleaseRequest b = new CmsReleaseRequest();
        b.decode(encoded);
        assertEquals(20, b.reqId.value());
        assertArrayEquals(new byte[]{0x0A, 0x0B, 0x0C, 0x0D}, b.assocId.value());
    }
}
