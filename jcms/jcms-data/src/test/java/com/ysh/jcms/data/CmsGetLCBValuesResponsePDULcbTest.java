// Auto-generated. Tests for CmsGetLCBValuesResponsePDULcb

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLCBValuesResponsePDULcbTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLCBValuesResponsePDULcb obj = new CmsGetLCBValuesResponsePDULcb();
        assertNull(obj.value);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLCBValuesResponsePDULcb obj = new CmsGetLCBValuesResponsePDULcb();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLCBValuesResponsePDULcb d = MAPPER.readValue(json, CmsGetLCBValuesResponsePDULcb.class);
        assertEquals(obj, d);
    }
}
