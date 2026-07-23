// Auto-generated. Tests for CmsGetLogicalDeviceDirectoryResponsePDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryResponsePDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogicalDeviceDirectoryResponsePDU obj = new CmsGetLogicalDeviceDirectoryResponsePDU();
        assertNotNull(obj.ln_reference);
        assertFalse(obj.more_follows);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectoryResponsePDU obj = new CmsGetLogicalDeviceDirectoryResponsePDU();
        obj.ln_reference = java.util.Collections.singletonList("test");
        obj.more_follows = true;
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalDeviceDirectoryResponsePDU d = MAPPER.readValue(json, CmsGetLogicalDeviceDirectoryResponsePDU.class);
        assertEquals(obj, d);
    }
}
