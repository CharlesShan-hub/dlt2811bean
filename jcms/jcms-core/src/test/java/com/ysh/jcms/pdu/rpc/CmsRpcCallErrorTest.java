package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsRpcCallErrorTest {
    @Test
    public void roundup() {
        CmsRpcCallError a = new CmsRpcCallError(CmsServiceError.CONTROL_MUST_BE_SELECTED);
        byte[] encoded = a.encode();

        CmsRpcCallError b = new CmsRpcCallError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
