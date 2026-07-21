// Auto-generated. Tests for CmsAbortRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsAbortRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsAbortRequestPDU obj = new CmsAbortRequestPDU();
        assertNull(obj.association_id);
        assertEquals(0, obj.reason);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsAbortRequestPDU obj = new CmsAbortRequestPDU();
        obj.association_id = new byte[0];
        obj.reason = 42;
        String json = MAPPER.writeValueAsString(obj);
        CmsAbortRequestPDU d = MAPPER.readValue(json, CmsAbortRequestPDU.class);
        assertEquals(obj, d);
    }
}
