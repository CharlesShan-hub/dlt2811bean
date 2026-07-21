// Auto-generated. Tests for CmsSelectResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectResponsePDU obj = new CmsSelectResponsePDU();
        assertNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectResponsePDU obj = new CmsSelectResponsePDU();
        obj.reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectResponsePDU d = MAPPER.readValue(json, CmsSelectResponsePDU.class);
        assertEquals(obj, d);
    }
}
