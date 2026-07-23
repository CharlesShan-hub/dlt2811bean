// Auto-generated. Tests for CmsGetRpcInterfaceDefinitionResponsePDUMethod

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcInterfaceDefinitionResponsePDUMethodTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcInterfaceDefinitionResponsePDUMethod obj = new CmsGetRpcInterfaceDefinitionResponsePDUMethod();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcInterfaceDefinitionResponsePDUMethod obj = new CmsGetRpcInterfaceDefinitionResponsePDUMethod();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcInterfaceDefinitionResponsePDUMethod d = MAPPER.readValue(json, CmsGetRpcInterfaceDefinitionResponsePDUMethod.class);
        assertEquals(obj, d);
    }
}
