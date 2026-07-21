// Auto-generated. Tests for CmsSelectRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsSelectRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsSelectRequestPDU obj = new CmsSelectRequestPDU();
        assertNull(obj.reference);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsSelectRequestPDU obj = new CmsSelectRequestPDU();
        obj.reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsSelectRequestPDU d = MAPPER.readValue(json, CmsSelectRequestPDU.class);
        assertEquals(obj, d);
    }
}
