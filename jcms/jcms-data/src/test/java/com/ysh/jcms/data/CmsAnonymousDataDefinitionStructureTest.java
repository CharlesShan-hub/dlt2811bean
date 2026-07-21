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
        if (obj.r_type == null) obj.r_type = new CmsDataDefinition();
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousDataDefinitionStructure d = MAPPER.readValue(json, CmsAnonymousDataDefinitionStructure.class);
        assertEquals(obj, d);
    }
}
