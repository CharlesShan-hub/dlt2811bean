// Auto-generated. Tests for CmsAnonymousGetAllDataValuesResponsePDUData

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAnonymousGetAllDataValuesResponsePDUDataTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAnonymousGetAllDataValuesResponsePDUData obj = new CmsAnonymousGetAllDataValuesResponsePDUData();
        assertNull(obj.reference);
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAnonymousGetAllDataValuesResponsePDUData obj = new CmsAnonymousGetAllDataValuesResponsePDUData();
        obj.reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsAnonymousGetAllDataValuesResponsePDUData d = MAPPER.readValue(json, CmsAnonymousGetAllDataValuesResponsePDUData.class);
        assertEquals(obj, d);
    }
}
