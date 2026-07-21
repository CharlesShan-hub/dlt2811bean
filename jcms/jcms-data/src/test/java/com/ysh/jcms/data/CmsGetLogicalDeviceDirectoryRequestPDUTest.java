// Auto-generated. Tests for CmsGetLogicalDeviceDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogicalDeviceDirectoryRequestPDU obj = new CmsGetLogicalDeviceDirectoryRequestPDU();
        assertNull(obj.ld_name);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectoryRequestPDU obj = new CmsGetLogicalDeviceDirectoryRequestPDU();
        obj.ld_name = "test";
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalDeviceDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetLogicalDeviceDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogicalDeviceDirectoryRequestPDU obj = new CmsGetLogicalDeviceDirectoryRequestPDU();
        obj.ld_name = "test";
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetLogicalDeviceDirectoryRequestPDU d = CmsGetLogicalDeviceDirectoryRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
