// Auto-generated. Tests for CmsGetEditSGValueRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetEditSGValueRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetEditSGValueRequestPDU obj = new CmsGetEditSGValueRequestPDU();
        assertNull(obj.data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetEditSGValueRequestPDU obj = new CmsGetEditSGValueRequestPDU();
        String json = MAPPER.writeValueAsString(obj);
        CmsGetEditSGValueRequestPDU d = MAPPER.readValue(json, CmsGetEditSGValueRequestPDU.class);
        assertEquals(obj, d);
    }
}
