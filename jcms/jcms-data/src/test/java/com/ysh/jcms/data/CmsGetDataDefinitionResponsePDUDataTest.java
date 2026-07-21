// Auto-generated. Tests for CmsGetDataDefinitionResponsePDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionResponsePDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDefinitionResponsePDUData obj = new CmsGetDataDefinitionResponsePDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDefinitionResponsePDUData obj = new CmsGetDataDefinitionResponsePDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDefinitionResponsePDUData d = MAPPER.readValue(json, CmsGetDataDefinitionResponsePDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDefinitionResponsePDUData obj = new CmsGetDataDefinitionResponsePDUData();
        byte[] data = obj.encode("uper");
        CmsGetDataDefinitionResponsePDUData d = CmsGetDataDefinitionResponsePDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
