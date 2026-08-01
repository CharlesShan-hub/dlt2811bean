package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.scalar.CmsString;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetRpcInterfaceDirectoryResponse a = new CmsGetRpcInterfaceDirectoryResponse()
            .reference(Arrays.asList(
                new CmsString().value("int1"),
                new CmsString().value("int2")))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDirectoryResponse b = new CmsGetRpcInterfaceDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
