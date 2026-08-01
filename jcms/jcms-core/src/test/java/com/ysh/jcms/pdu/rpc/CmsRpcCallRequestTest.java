package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.choice.CmsRpcCallReqChoice;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsRpcCallRequestTest {
    @Test
    public void roundup() {
        CmsRpcCallRequest a = new CmsRpcCallRequest()
            .method("myMethod")
            .req(new CmsRpcCallReqChoice().altReqData(new CmsData().alt_boolean(true)));
        byte[] encoded = a.encode();

        CmsRpcCallRequest b = new CmsRpcCallRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
