// Auto-generated. Tests for CmsGetSGCBValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetSGCBValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetSGCBValuesResponsePDU obj = new CmsGetSGCBValuesResponsePDU();
        assertNull(obj.sgscb);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetSGCBValuesResponsePDU obj = new CmsGetSGCBValuesResponsePDU();
        obj.sgscb = java.util.Collections.singletonList(new CmsAnonymousGetSGCBValuesResponsePDUSgscb());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetSGCBValuesResponsePDU d = MAPPER.readValue(json, CmsGetSGCBValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
