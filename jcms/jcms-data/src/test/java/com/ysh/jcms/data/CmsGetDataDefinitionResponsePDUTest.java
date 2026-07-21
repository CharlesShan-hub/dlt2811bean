// Auto-generated. Tests for CmsGetDataDefinitionResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDefinitionResponsePDU obj = new CmsGetDataDefinitionResponsePDU();
        assertNull(obj.data);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDefinitionResponsePDU obj = new CmsGetDataDefinitionResponsePDU();
        obj.data = java.util.Collections.singletonList(new CmsAnonymousGetDataDefinitionResponsePDUData());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDefinitionResponsePDU d = MAPPER.readValue(json, CmsGetDataDefinitionResponsePDU.class);
        assertEquals(obj, d);
    }
}
