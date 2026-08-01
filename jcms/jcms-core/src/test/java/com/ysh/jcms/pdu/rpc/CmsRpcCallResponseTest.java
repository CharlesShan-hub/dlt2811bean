package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.choice.CmsData;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsRpcCallResponseTest {
    @Test
    public void roundup() {
        CmsRpcCallResponse a = new CmsRpcCallResponse()
            .rspData(new CmsData().alt_int32(12345))
            .nextCallID("next".getBytes());
        byte[] encoded = a.encode();

        CmsRpcCallResponse b = new CmsRpcCallResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
