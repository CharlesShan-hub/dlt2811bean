package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionErrorTest {
    @Test
    public void roundup() {
        CmsGetRpcMethodDefinitionError a = new CmsGetRpcMethodDefinitionError(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDefinitionError b = new CmsGetRpcMethodDefinitionError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
