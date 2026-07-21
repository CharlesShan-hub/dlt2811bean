// Auto-generated. Tests for CmsAnonymousDataDefinitionStructure

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousDataDefinitionStructureTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousDataDefinitionStructure obj = new CmsAnonymousDataDefinitionStructure();
        assertNull(obj.name);
        assertNull(obj.fc);
        assertNull(obj.r_type);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousDataDefinitionStructure obj = new CmsAnonymousDataDefinitionStructure();
        obj.name = "test";
        obj.fc = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousDataDefinitionStructure d = MAPPER.readValue(json, CmsAnonymousDataDefinitionStructure.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsAnonymousDataDefinitionStructure obj = new CmsAnonymousDataDefinitionStructure();
        obj.name = "test";
        obj.fc = "test";
        byte[] data = obj.encode("uper");
        CmsAnonymousDataDefinitionStructure d = CmsAnonymousDataDefinitionStructure.decode("uper", data);
        assertEquals(obj, d);
    }
}
