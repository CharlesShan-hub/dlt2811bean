package com.ysh.jcms.core.pdu.rpc;

import com.ysh.jcms.core.data.choice.CmsDataDefinition;
import com.ysh.jcms.core.data.choice.CmsRpcMethodDefChoice;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.rpc.CmsRpcMethodDef;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionResponseTest {
    @Test
    public void roundup() {
        CmsGetRpcMethodDefinitionResponse a = new CmsGetRpcMethodDefinitionResponse()
            .reference(Arrays.asList(
                new CmsRpcMethodDefChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE),
                new CmsRpcMethodDefChoice().altMethod(new CmsRpcMethodDef()
                    .version(1L)
                    .timeout(30L)
                    .request(new CmsDataDefinition().alt_octet_string_len(4))
                    .response(new CmsDataDefinition().alt_octet_string_len(4)))))
            .moreFollows(false);
        byte[] encoded = a.encode();

        CmsGetRpcMethodDefinitionResponse b = new CmsGetRpcMethodDefinitionResponse();
        b.decode(encoded);
        assertEquals(a, b);
    }
}
