package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.core.data.scalar.CmsString;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionRequestTest {
    @Test
    public void roundup() {
        CmsGetRpcMethodDefinitionRequest a = new CmsGetRpcMethodDefinitionRequest()
            .reference(Arrays.asList(
                new CmsString().value("md1"),
                new CmsString().value("md2")));
        byte[] encoded = a.encode();

        CmsGetRpcMethodDefinitionRequest b = new CmsGetRpcMethodDefinitionRequest();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
