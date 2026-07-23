// Auto-generated. Tests for CmsDataDefinitionStructure

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDataDefinitionStructureTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDataDefinitionStructure obj = new CmsDataDefinitionStructure();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDataDefinitionStructure obj = new CmsDataDefinitionStructure();
        String json = MAPPER.writeValueAsString(obj);
        CmsDataDefinitionStructure d = MAPPER.readValue(json, CmsDataDefinitionStructure.class);
        assertEquals(obj, d);
    }
}
