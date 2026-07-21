// Auto-generated. Tests for CmsGetRpcInterfaceDefinitionResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDefinitionResponsePDU obj = new CmsGetRpcInterfaceDefinitionResponsePDU();
        assertNull(obj.method);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinitionResponsePDU obj = new CmsGetRpcInterfaceDefinitionResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDefinitionResponsePDU d = MAPPER.readValue(json, CmsGetRpcInterfaceDefinitionResponsePDU.class);
        assertEquals(obj, d);
    }
}
