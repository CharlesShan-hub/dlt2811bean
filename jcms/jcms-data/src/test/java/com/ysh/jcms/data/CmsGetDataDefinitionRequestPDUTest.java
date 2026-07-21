// Auto-generated. Tests for CmsGetDataDefinitionRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDefinitionRequestPDU obj = new CmsGetDataDefinitionRequestPDU();
        assertNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDefinitionRequestPDU obj = new CmsGetDataDefinitionRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDefinitionRequestPDU d = MAPPER.readValue(json, CmsGetDataDefinitionRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDefinitionRequestPDU obj = new CmsGetDataDefinitionRequestPDU();
        byte[] data = obj.encode("uper");
        CmsGetDataDefinitionRequestPDU d = CmsGetDataDefinitionRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
