// Auto-generated. Tests for CmsGetDataDefinitionRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDefinitionRequestPDUData obj = new CmsGetDataDefinitionRequestPDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDefinitionRequestPDUData obj = new CmsGetDataDefinitionRequestPDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDefinitionRequestPDUData d = MAPPER.readValue(json, CmsGetDataDefinitionRequestPDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDefinitionRequestPDUData obj = new CmsGetDataDefinitionRequestPDUData();
        byte[] data = obj.encode("uper");
        CmsGetDataDefinitionRequestPDUData d = CmsGetDataDefinitionRequestPDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
