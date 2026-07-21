// Auto-generated. Tests for CmsGetURCBValuesResponsePDUUrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetURCBValuesResponsePDUUrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetURCBValuesResponsePDUUrcb obj = new CmsGetURCBValuesResponsePDUUrcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetURCBValuesResponsePDUUrcb obj = new CmsGetURCBValuesResponsePDUUrcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetURCBValuesResponsePDUUrcb d = MAPPER.readValue(json, CmsGetURCBValuesResponsePDUUrcb.class);
        assertEquals(obj, d);
    }
}
