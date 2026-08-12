package com.ysh.jcms.core.pdu.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsOperateResponseTest {
    @Test
    public void roundup() {
        CmsOperateResponse a = new CmsOperateResponse().reference("resp2".getBytes());
        byte[] encoded = a.encode();

        CmsOperateResponse b = new CmsOperateResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
