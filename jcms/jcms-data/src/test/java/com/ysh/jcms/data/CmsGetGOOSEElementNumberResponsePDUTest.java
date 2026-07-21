// Auto-generated. Tests for CmsGetGOOSEElementNumberResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetGOOSEElementNumberResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetGOOSEElementNumberResponsePDU obj = new CmsGetGOOSEElementNumberResponsePDU();
        assertNull(obj.gocb_reference);
        assertEquals(0, obj.conf_rev);
        assertNull(obj.dat_set);
        assertNotNull(obj.member_offset);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetGOOSEElementNumberResponsePDU obj = new CmsGetGOOSEElementNumberResponsePDU();
        obj.gocb_reference = "test";
        obj.conf_rev = 1;
        obj.dat_set = "test";
        obj.member_offset = java.util.Collections.singletonList(Integer.valueOf(1));
        String json = MAPPER.writeValueAsString(obj);
        CmsGetGOOSEElementNumberResponsePDU d = MAPPER.readValue(json, CmsGetGOOSEElementNumberResponsePDU.class);
        assertEquals(obj, d);
    }
}
