// Auto-generated. Tests for CmsGetLogicalDeviceDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalDeviceDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogicalDeviceDirectoryErrorPDU obj = new CmsGetLogicalDeviceDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetLogicalDeviceDirectoryErrorPDU obj = new CmsGetLogicalDeviceDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogicalDeviceDirectoryErrorPDU obj = new CmsGetLogicalDeviceDirectoryErrorPDU(1);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalDeviceDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetLogicalDeviceDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogicalDeviceDirectoryErrorPDU obj = new CmsGetLogicalDeviceDirectoryErrorPDU(1);
        byte[] data = obj.encode("uper");
        CmsGetLogicalDeviceDirectoryErrorPDU d = CmsGetLogicalDeviceDirectoryErrorPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
