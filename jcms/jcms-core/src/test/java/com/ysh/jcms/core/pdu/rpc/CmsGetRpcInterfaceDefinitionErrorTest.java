package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionErrorTest {
    @Test
    public void roundup() {
        CmsGetRpcInterfaceDefinitionError a = new CmsGetRpcInterfaceDefinitionError(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDefinitionError b = new CmsGetRpcInterfaceDefinitionError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
