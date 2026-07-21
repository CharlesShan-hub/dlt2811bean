// Auto-generated. Tests for CmsGetServerDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetServerDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetServerDirectoryRequestPDU obj = new CmsGetServerDirectoryRequestPDU();
        assertEquals(0, obj.object_class);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetServerDirectoryRequestPDU obj = new CmsGetServerDirectoryRequestPDU();
        obj.object_class = 1;
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetServerDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetServerDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
}
