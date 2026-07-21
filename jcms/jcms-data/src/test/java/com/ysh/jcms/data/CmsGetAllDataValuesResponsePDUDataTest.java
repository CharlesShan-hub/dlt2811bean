// Auto-generated. Tests for CmsGetAllDataValuesResponsePDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetAllDataValuesResponsePDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetAllDataValuesResponsePDUData obj = new CmsGetAllDataValuesResponsePDUData();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetAllDataValuesResponsePDUData obj = new CmsGetAllDataValuesResponsePDUData();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetAllDataValuesResponsePDUData d = MAPPER.readValue(json, CmsGetAllDataValuesResponsePDUData.class);
        assertEquals(obj, d);
    }
}
