// Auto-generated. Tests for CmsGetAllDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionResponsePDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataDefinitionResponsePDUData obj = new CmsGetAllDataDefinitionResponsePDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataDefinitionResponsePDUData obj = new CmsGetAllDataDefinitionResponsePDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataDefinitionResponsePDUData d = MAPPER.readValue(json, CmsGetAllDataDefinitionResponsePDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetAllDataDefinitionResponsePDUData obj = new CmsGetAllDataDefinitionResponsePDUData();
        byte[] data = obj.encode("uper");
        CmsGetAllDataDefinitionResponsePDUData d = CmsGetAllDataDefinitionResponsePDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
