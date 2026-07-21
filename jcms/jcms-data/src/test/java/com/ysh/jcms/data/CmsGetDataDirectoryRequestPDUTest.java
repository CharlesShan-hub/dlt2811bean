// Auto-generated. Tests for CmsGetDataDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetDataDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetDataDirectoryRequestPDU obj = new CmsGetDataDirectoryRequestPDU();
        assertNull(obj.data_reference);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetDataDirectoryRequestPDU obj = new CmsGetDataDirectoryRequestPDU();
        obj.data_reference = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetDataDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetDataDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
}
