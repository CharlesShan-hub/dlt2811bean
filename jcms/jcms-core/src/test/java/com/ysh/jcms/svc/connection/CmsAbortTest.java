package com.ysh.jcms.svc.connection;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAbortTest {
    @Test
    public void roundtrip() {
        CmsAbort a = new CmsAbort();
        a.reqId.value(100);
        a.assocId.value(new byte[]{0x01, 0x02, 0x03, 0x04});
        a.reason.value(CmsAbortReason.INVALID_ARGUMENT);
        byte[] encoded = a.encode();

        CmsAbort b = new CmsAbort();
        b.decode(encoded);
        assertEquals(100, b.reqId.value());
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, b.assocId.value());
        assertEquals(CmsAbortReason.INVALID_ARGUMENT, b.reason.value());
    }
}
