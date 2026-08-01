package com.ysh.jcms.pdu.rpc;

import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionRequestTest {
    @Test
    public void roundup() {
        CmsGetRpcInterfaceDefinitionRequest a = new CmsGetRpcInterfaceDefinitionRequest()
            .interfaceName("ifDef")
            .referenceAfter("after");
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDefinitionRequest b = new CmsGetRpcInterfaceDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
