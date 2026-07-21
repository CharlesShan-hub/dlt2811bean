// Auto-generated. Tests for CmsAnonymousGetDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetDataDefinitionResponsePDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetDataDefinitionResponsePDUData obj = new CmsAnonymousGetDataDefinitionResponsePDUData();
        assertNull(obj.cdc_type);
        assertNull(obj.definition);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetDataDefinitionResponsePDUData obj = new CmsAnonymousGetDataDefinitionResponsePDUData();
        obj.cdc_type = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetDataDefinitionResponsePDUData d = MAPPER.readValue(json, CmsAnonymousGetDataDefinitionResponsePDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetDataDefinitionResponsePDUData obj = new CmsAnonymousGetDataDefinitionResponsePDUData();
        obj.cdc_type = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousGetDataDefinitionResponsePDUData d = CmsAnonymousGetDataDefinitionResponsePDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
