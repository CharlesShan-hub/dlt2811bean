// Auto-generated. Tests for CmsGetLogicalNodeDirectoryErrorPDU

package com.ysh.jcms.data;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;

public class CmsGetLogicalNodeDirectoryErrorPDUTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDefault() {
        CmsGetLogicalNodeDirectoryErrorPDU obj = new CmsGetLogicalNodeDirectoryErrorPDU();
        assertEquals(0, obj.value);
    }

    @Test
    public void testValueConstructor() {
        CmsGetLogicalNodeDirectoryErrorPDU obj = new CmsGetLogicalNodeDirectoryErrorPDU(42);
        assertNotNull(obj);
    }

    @Test
    public void testJsonRoundTrip() throws Exception {
        CmsGetLogicalNodeDirectoryErrorPDU obj = new CmsGetLogicalNodeDirectoryErrorPDU(42);
        String json = MAPPER.writeValueAsString(obj);
        CmsGetLogicalNodeDirectoryErrorPDU d = MAPPER.readValue(json, CmsGetLogicalNodeDirectoryErrorPDU.class);
        assertEquals(obj, d);
    }
}
