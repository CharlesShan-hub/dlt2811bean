// Auto-generated. Tests for CmsGetAllDataDefinitionRequestPDUReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionRequestPDUReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceldName() throws Exception {
        CmsGetAllDataDefinitionRequestPDUReference obj = new CmsGetAllDataDefinitionRequestPDUReference();
        obj._choice = "ldName";
        obj.ldName = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataDefinitionRequestPDUReference d = MAPPER.readValue(json, CmsGetAllDataDefinitionRequestPDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicelnReference() throws Exception {
        CmsGetAllDataDefinitionRequestPDUReference obj = new CmsGetAllDataDefinitionRequestPDUReference();
        obj._choice = "lnReference";
        obj.lnReference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataDefinitionRequestPDUReference d = MAPPER.readValue(json, CmsGetAllDataDefinitionRequestPDUReference.class);
        assertEquals(obj, d);
    }

}
