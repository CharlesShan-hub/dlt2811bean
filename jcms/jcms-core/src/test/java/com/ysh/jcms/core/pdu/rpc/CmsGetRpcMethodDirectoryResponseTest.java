package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.core.data.scalar.CmsString;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDirectoryResponseTest {
    @Test
    public void roundup() {
        CmsGetRpcMethodDirectoryResponse a = new CmsGetRpcMethodDirectoryResponse()
            .reference(Arrays.asList(
                new CmsString().value("m1"),
                new CmsString().value("m2")))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDirectoryResponse b = new CmsGetRpcMethodDirectoryResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
