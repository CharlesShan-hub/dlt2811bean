// Auto-generated. Tests for CmsGetRpcMethodDefinitionRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetRpcMethodDefinitionRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetRpcMethodDefinitionRequestPDU obj = new CmsGetRpcMethodDefinitionRequestPDU();
        assertNotNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetRpcMethodDefinitionRequestPDU obj = new CmsGetRpcMethodDefinitionRequestPDU();
        obj.reference = java.util.Collections.singletonList("test");
        String json = MAPPER.writeValueAsString(obj);
        CmsGetRpcMethodDefinitionRequestPDU d = MAPPER.readValue(json, CmsGetRpcMethodDefinitionRequestPDU.class);
        assertEquals(obj, d);
    }
}
