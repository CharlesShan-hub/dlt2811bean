// Auto-generated. Tests for CmsGetLCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLCBValuesResponsePDU obj = new CmsGetLCBValuesResponsePDU();
        assertNull(obj.lcb);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLCBValuesResponsePDU obj = new CmsGetLCBValuesResponsePDU();
        obj.lcb = java.util.Collections.singletonList(new CmsAnonymousGetLCBValuesResponsePDULcb());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLCBValuesResponsePDU d = MAPPER.readValue(json, CmsGetLCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
