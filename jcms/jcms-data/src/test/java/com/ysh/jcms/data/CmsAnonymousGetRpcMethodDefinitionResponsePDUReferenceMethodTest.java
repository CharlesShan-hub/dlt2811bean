// Auto-generated. Tests for CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethodTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod obj = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod();
        assertEquals(0, obj.version);
        assertEquals(0, obj.timeout);
        assertNull(obj.request);
        assertNull(obj.response);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod obj = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod();
        obj.version = 42;
        obj.timeout = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod d = MAPPER.readValue(json, CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod obj = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod();
        obj.version = 42;
        obj.timeout = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod d = CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod.decode("uper", data);
        assertEquals(obj, d);
    }
}
