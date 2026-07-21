// Auto-generated. Tests for CmsGetBRCBValuesResponsePDUBrcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetBRCBValuesResponsePDUBrcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetBRCBValuesResponsePDUBrcb obj = new CmsGetBRCBValuesResponsePDUBrcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetBRCBValuesResponsePDUBrcb obj = new CmsGetBRCBValuesResponsePDUBrcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetBRCBValuesResponsePDUBrcb d = MAPPER.readValue(json, CmsGetBRCBValuesResponsePDUBrcb.class);
        assertEquals(obj, d);
    }
}
