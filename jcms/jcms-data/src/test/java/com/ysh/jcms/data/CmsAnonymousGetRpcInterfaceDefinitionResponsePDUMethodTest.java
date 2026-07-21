// Auto-generated. Tests for CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethodTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod obj = new CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod();
        assertNull(obj.name);
        assertEquals(0, obj.version);
        assertEquals(0, obj.timeout);
        assertNull(obj.request);
        assertNull(obj.response);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod obj = new CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod();
        obj.name = "test";
        obj.version = 42;
        obj.timeout = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod d = MAPPER.readValue(json, CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod obj = new CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod();
        obj.name = "test";
        obj.version = 42;
        obj.timeout = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod d = CmsAnonymousGetRpcInterfaceDefinitionResponsePDUMethod.decode("uper", data);
        assertEquals(obj, d);
    }
}
