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
        if (obj.reference == null) obj.reference = new CmsGetLogicalNodeDirectoryRequestPDUReference();
        obj.acsi_class = 1;
        obj.reference_after = "test";
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalNodeDirectoryRequestPDU d = MAPPER.readValue(json, CmsGetLogicalNodeDirectoryRequestPDU.class);
        assertEquals(obj, d);
    }
}
