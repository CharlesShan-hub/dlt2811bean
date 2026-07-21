// Auto-generated. Tests for CmsGetRpcMethodDefinitionResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDefinitionResponsePDU obj = new CmsGetRpcMethodDefinitionResponsePDU();
        assertNull(obj.reference);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDefinitionResponsePDU obj = new CmsGetRpcMethodDefinitionResponsePDU();
        obj.reference = java.util.Collections.singletonList(new CmsAnonymousGetRpcMethodDefinitionResponsePDUReference());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDefinitionResponsePDU d = MAPPER.readValue(json, CmsGetRpcMethodDefinitionResponsePDU.class);
        assertEquals(obj, d);
    }
}
