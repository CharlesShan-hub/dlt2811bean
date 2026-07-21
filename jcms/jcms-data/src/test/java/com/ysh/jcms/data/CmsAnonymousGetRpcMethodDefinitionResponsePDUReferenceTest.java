// Auto-generated. Tests for CmsAnonymousGetRpcMethodDefinitionResponsePDUReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReference obj = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReference();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReference d = MAPPER.readValue(json, CmsAnonymousGetRpcMethodDefinitionResponsePDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicemethod() throws Exception {
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReference obj = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReference();
        obj._choice = "method";
        obj.method = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReferenceMethod();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReference d = MAPPER.readValue(json, CmsAnonymousGetRpcMethodDefinitionResponsePDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReference obj = new CmsAnonymousGetRpcMethodDefinitionResponsePDUReference();
        obj._choice = "error";
        obj.error = 42;
        byte[] data = obj.encode("uper");
        CmsAnonymousGetRpcMethodDefinitionResponsePDUReference d = CmsAnonymousGetRpcMethodDefinitionResponsePDUReference.decode("uper", data);
        assertEquals(obj, d);
    }
}
