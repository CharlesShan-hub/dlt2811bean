package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDirectoryErrorTest {
    @Test
    public void roundup() {
        CmsGetRpcMethodDirectoryError a = new CmsGetRpcMethodDirectoryError(CmsServiceError.ACCESS_VIOLATION);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDirectoryError b = new CmsGetRpcMethodDirectoryError();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
