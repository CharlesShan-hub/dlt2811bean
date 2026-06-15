package com.ysh.jcms.svc.connection;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAbortTest {
    @Test
    public void roundtrip() {
        CmsAbort a = new CmsAbort()
            .reqId(100)
            .assocId(new byte[]{0x01, 0x02, 0x03, 0x04})
            .reason(CmsAbortReason.INVALID_ARGUMENT);
        byte[] encoded = a.encode();

        CmsAbort b = new CmsAbort();
        b.decode(encoded);
        assertEquals(100, b.reqId.value());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, b.assocId.value());
        assertEquals(CmsAbortReason.INVALID_ARGUMENT, b.reason.value());
    }
}
