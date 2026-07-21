// Auto-generated. Tests for CmsGetRpcMethodDefinitionResponsePDUReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionResponsePDUReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDefinitionResponsePDUReference obj = new CmsGetRpcMethodDefinitionResponsePDUReference();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDefinitionResponsePDUReference obj = new CmsGetRpcMethodDefinitionResponsePDUReference();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDefinitionResponsePDUReference d = MAPPER.readValue(json, CmsGetRpcMethodDefinitionResponsePDUReference.class);
        assertEquals(obj, d);
    }
}
