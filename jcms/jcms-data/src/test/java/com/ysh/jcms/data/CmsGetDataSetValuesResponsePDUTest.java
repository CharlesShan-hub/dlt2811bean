// Auto-generated. Tests for CmsGetDataSetValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataSetValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataSetValuesResponsePDU obj = new CmsGetDataSetValuesResponsePDU();
        assertNotNull(obj.value);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataSetValuesResponsePDU obj = new CmsGetDataSetValuesResponsePDU();
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataSetValuesResponsePDU d = MAPPER.readValue(json, CmsGetDataSetValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
