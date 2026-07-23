// Auto-generated. Tests for CmsGetAllDataDefinitionRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataDefinitionRequestPDU obj = new CmsGetAllDataDefinitionRequestPDU();
        assertNull(obj.reference);
        assertNull(obj.fc);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataDefinitionRequestPDU obj = new CmsGetAllDataDefinitionRequestPDU();
        if (obj.reference == null) obj.reference = new CmsGetAllDataDefinitionRequestPDUReference();
        obj.fc = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataDefinitionRequestPDU d = MAPPER.readValue(json, CmsGetAllDataDefinitionRequestPDU.class);
        assertEquals(obj, d);
    }
}
