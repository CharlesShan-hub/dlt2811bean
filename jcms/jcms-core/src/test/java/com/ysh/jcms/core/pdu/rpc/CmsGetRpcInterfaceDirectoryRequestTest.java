package com.ysh.jcms.core.pdu.rpc;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDirectoryRequestTest {
    @Test
    public void roundup() {
        CmsGetRpcInterfaceDirectoryRequest a = new CmsGetRpcInterfaceDirectoryRequest()
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDirectoryRequest b = new CmsGetRpcInterfaceDirectoryRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
