package com.ysh.jcms.pdu.rpc;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDirectoryRequestTest {
    @Test
    public void roundup() {
        CmsGetRpcMethodDirectoryRequest a = new CmsGetRpcMethodDirectoryRequest()
            .interfaceName("iface")
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetRpcMethodDirectoryRequest b = new CmsGetRpcMethodDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
