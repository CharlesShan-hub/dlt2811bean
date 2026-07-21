// Auto-generated. Tests for CmsGetAllDataDefinitionErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataDefinitionErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataDefinitionErrorPDU obj = new CmsGetAllDataDefinitionErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetAllDataDefinitionErrorPDU obj = new CmsGetAllDataDefinitionErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataDefinitionErrorPDU obj = new CmsGetAllDataDefinitionErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataDefinitionErrorPDU d = MAPPER.readValue(json, CmsGetAllDataDefinitionErrorPDU.class);
        assertEquals(obj, d);
    }
}
