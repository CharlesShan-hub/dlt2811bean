// Auto-generated. Tests for CmsGetLogicalNodeDirectoryRequestPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryRequestPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogicalNodeDirectoryRequestPDU obj = new CmsGetLogicalNodeDirectoryRequestPDU();
        assertNull(obj.reference);
        assertEquals(0, obj.acsi_class);
        assertNull(obj.reference_after);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectoryRequestPDU obj = new CmsGetLogicalNodeDirectoryRequestPDU();
        obj.acsi_class = 42;
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalNodeDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetLogicalNodeDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
    @Test
    public void testEncodeDecode() throws Exception {
        CmsGetLogicalNodeDirectoryRequestPDU obj = new CmsGetLogicalNodeDirectoryRequestPDU();
        obj.acsi_class = 42;
        obj.reference_after = "test";
        byte[] data = obj.encode("uper");
        CmsGetLogicalNodeDirectoryRequestPDU d = CmsGetLogicalNodeDirectoryRequestPDU.decode("uper", data);
        assertEquals(obj, d);
    }
}
