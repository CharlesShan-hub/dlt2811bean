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
        obj.version = 1;
        obj.timeout = 1;
        if (obj.request == null) obj.request = new CmsDataDefinition();
        if (obj.response == null) obj.response = new CmsDataDefinition();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod d = MAPPER.readValue(json, CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod.class);
        assertEquals(obj, d);
    }
}
