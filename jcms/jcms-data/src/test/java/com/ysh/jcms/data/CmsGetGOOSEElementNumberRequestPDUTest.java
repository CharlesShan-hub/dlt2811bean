// Auto-generated. Tests for CmsGetGOOSEElementNumberRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGOOSEElementNumberRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGOOSEElementNumberRequestPDU obj = new CmsGetGOOSEElementNumberRequestPDU();
        assertNull(obj.gocb_reference);
        assertNull(obj.member_data);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGOOSEElementNumberRequestPDU obj = new CmsGetGOOSEElementNumberRequestPDU();
        obj.gocb_reference = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGOOSEElementNumberRequestPDU d = MAPPER.readValue(json, CmsGetGOOSEElementNumberRequestPDU.class);
        assertEquals(obj, d);
    }
}
