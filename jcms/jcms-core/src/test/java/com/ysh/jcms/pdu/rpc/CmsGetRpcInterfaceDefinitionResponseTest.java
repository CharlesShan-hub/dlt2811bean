package com.ysh.jcms.pdu.rpc;

import com.ysh.jcms.data.choice.CmsDataDefinition;
import com.ysh.jcms.data.sequence.rpc.CmsRpcMethodEntry;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionResponseTest {
    @Test
    public void roundup() {
        CmsGetRpcInterfaceDefinitionResponse a = new CmsGetRpcInterfaceDefinitionResponse()
            .method(Arrays.asList(
                new CmsRpcMethodEntry()
                    .name("m1")
                    .version(1L)
                    .timeout(30L)
                    .request(new CmsDataDefinition().alt_octet_string_len(4))
                    .response(new CmsDataDefinition().alt_octet_string_len(4)),
                new CmsRpcMethodEntry()
                    .name("m2")
                    .version(2L)
                    .timeout(60L)
                    .request(new CmsDataDefinition().alt_visible_string_len(8))
                    .response(new CmsDataDefinition().alt_visible_string_len(8))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetRpcInterfaceDefinitionResponse b = new CmsGetRpcInterfaceDefinitionResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
