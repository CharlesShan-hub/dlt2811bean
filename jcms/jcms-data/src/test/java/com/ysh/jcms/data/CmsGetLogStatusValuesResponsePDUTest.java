// Auto-generated. Tests for CmsGetLogStatusValuesResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogStatusValuesResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogStatusValuesResponsePDU obj = new CmsGetLogStatusValuesResponsePDU();
        assertNull(obj.log);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogStatusValuesResponsePDU obj = new CmsGetLogStatusValuesResponsePDU();
        obj.log = java.util.Collections.singletonList(new CmsAnonymousGetLogStatusValuesResponsePDULog());
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogStatusValuesResponsePDU d = MAPPER.readValue(json, CmsGetLogStatusValuesResponsePDU.class);
        assertEquals(obj, d);
    }
}
