// Auto-generated. Tests for CmsGetAllDataDefinitionResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataDefinitionResponsePDU obj = new CmsGetAllDataDefinitionResponsePDU();
        assertNull(obj.data);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataDefinitionResponsePDU obj = new CmsGetAllDataDefinitionResponsePDU();
        obj.data = java.util.Collections.singletonList(new CmsAnonymousGetAllDataDefinitionResponsePDUData());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataDefinitionResponsePDU d = MAPPER.readValue(json, CmsGetAllDataDefinitionResponsePDU.class);
        assertEquals(obj, d);
    }
}
