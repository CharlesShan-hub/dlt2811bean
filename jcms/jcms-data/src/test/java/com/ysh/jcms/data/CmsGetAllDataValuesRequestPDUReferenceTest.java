// Auto-generated. Tests for CmsGetAllDataValuesRequestPDUReference

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesRequestPDUReferenceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testChoiceldName() throws Exception {
        CmsGetAllDataValuesRequestPDUReference obj = new CmsGetAllDataValuesRequestPDUReference();
        obj._choice = "ldName";
        obj.ldName = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataValuesRequestPDUReference d = MAPPER.readValue(json, CmsGetAllDataValuesRequestPDUReference.class);
        assertEquals(obj, d);
    }

    @Test
    public void testChoicelnReference() throws Exception {
        CmsGetAllDataValuesRequestPDUReference obj = new CmsGetAllDataValuesRequestPDUReference();
        obj._choice = "lnReference";
        obj.lnReference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataValuesRequestPDUReference d = MAPPER.readValue(json, CmsGetAllDataValuesRequestPDUReference.class);
        assertEquals(obj, d);
    }

}
