// Auto-generated. Tests for CmsAnonymousGetDataDefinitionRequestPDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetDataDefinitionRequestPDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetDataDefinitionRequestPDUData obj = new CmsAnonymousGetDataDefinitionRequestPDUData();
        assertNull(obj.reference);
        assertNull(obj.fc);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetDataDefinitionRequestPDUData obj = new CmsAnonymousGetDataDefinitionRequestPDUData();
        obj.reference = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetDataDefinitionRequestPDUData d = MAPPER.readValue(json, CmsAnonymousGetDataDefinitionRequestPDUData.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousGetDataDefinitionRequestPDUData obj = new CmsAnonymousGetDataDefinitionRequestPDUData();
        obj.reference = "test";
        obj.fc = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousGetDataDefinitionRequestPDUData d = CmsAnonymousGetDataDefinitionRequestPDUData.decode("uper", data);
        assertEquals(obj, d);
    }
}
