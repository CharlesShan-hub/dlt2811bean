// Auto-generated. Tests for CmsGetDataDefinitionErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDefinitionErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDefinitionErrorPDU obj = new CmsGetDataDefinitionErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetDataDefinitionErrorPDU obj = new CmsGetDataDefinitionErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDefinitionErrorPDU obj = new CmsGetDataDefinitionErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDefinitionErrorPDU d = MAPPER.readValue(json, CmsGetDataDefinitionErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetDataDefinitionErrorPDU obj = new CmsGetDataDefinitionErrorPDU(42);
        byte[] data = obj.encode("uper");
        CmsGetDataDefinitionErrorPDU d = CmsGetDataDefinitionErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
