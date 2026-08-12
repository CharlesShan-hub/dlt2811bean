package com.ysh.jcms.core.pdu.connection;

import com.ysh.jcms.core.data.enumerate.CmsAbortReason;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsAbortTest {
    @Test
    public void roundup() {
        CmsAbort a = new CmsAbort()
            .associationId(new byte[]{0x01, 0x02, 0x03, 0x04})
            .reason(CmsAbortReason.INVALID_ARGUMENT);
        byte[] encoded = a.encode();

        CmsAbort b = new CmsAbort();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
