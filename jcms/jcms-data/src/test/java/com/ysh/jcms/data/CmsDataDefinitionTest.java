// Auto-generated. Tests for CmsDataDefinition

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDataDefinitionTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceerror() throws Exception {
        CmsDataDefinition obj = new CmsDataDefinition();
        obj._choice = "error";
        obj.error = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsDataDefinition d = MAPPER.readValue(json, CmsDataDefinition.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicearray() throws Exception {
        CmsDataDefinition obj = new CmsDataDefinition();
        obj._choice = "array";
        obj.array = new CmsDataDefinitionArray();
        String json = MAPPER.writeValueAsString(obj);
        CmsDataDefinition d = MAPPER.readValue(json, CmsDataDefinition.class);
        assertEquals(obj, d);
    }

}
