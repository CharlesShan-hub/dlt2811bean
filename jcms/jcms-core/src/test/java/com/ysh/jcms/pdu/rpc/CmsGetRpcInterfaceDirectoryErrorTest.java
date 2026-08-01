package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetRpcInterfaceDirectoryError a = new CmsGetRpcInterfaceDirectoryError(CmsServiceError.INSTANCE_LOCKED_BY_OTHER_CLIENT);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDirectoryError b = new CmsGetRpcInterfaceDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
