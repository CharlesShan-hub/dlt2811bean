// Auto-generated. Tests for CmsDataDefinitionArray

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsDataDefinitionArrayTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsDataDefinitionArray obj = new CmsDataDefinitionArray();
        assertEquals(0, obj.number_of_element);
        assertNull(obj.element_type);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsDataDefinitionArray obj = new CmsDataDefinitionArray();
        obj.number_of_element = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsDataDefinitionArray d = MAPPER.readValue(json, CmsDataDefinitionArray.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsDataDefinitionArray obj = new CmsDataDefinitionArray();
        obj.number_of_element = 42;
        byte[] data = obj.encode("uper");
        CmsDataDefinitionArray d = CmsDataDefinitionArray.decode("uper", data);
        assertEquals(obj, d);
    }
}
