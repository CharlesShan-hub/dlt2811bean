// Auto-generated. Tests for CmsAnonymousGetAllDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetAllDataDefinitionResponsePDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetAllDataDefinitionResponsePDUData obj = new CmsAnonymousGetAllDataDefinitionResponsePDUData();
        assertNull(obj.reference);
        assertNull(obj.cdc_type);
        assertNull(obj.definition);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetAllDataDefinitionResponsePDUData obj = new CmsAnonymousGetAllDataDefinitionResponsePDUData();
        obj.reference = "test";
        obj.cdc_type = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetAllDataDefinitionResponsePDUData d = MAPPER.readValue(json, CmsAnonymousGetAllDataDefinitionResponsePDUData.class);
        assertEquals(obj, d);
    }
}
